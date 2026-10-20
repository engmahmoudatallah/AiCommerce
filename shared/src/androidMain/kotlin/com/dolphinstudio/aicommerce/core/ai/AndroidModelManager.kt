package com.dolphinstudio.aicommerce.core.ai

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class AndroidModelManager(
    context: Context,
) : ModelManager {
    private val appContext = context.applicationContext
    private val modelFile = File(appContext.filesDir, EmbeddingModels.FILE_NAME)
    private val progress = MutableStateFlow<Float?>(null)

    override val downloadProgress: StateFlow<Float?> = progress.asStateFlow()

    override suspend fun checkIfModelAvailable(): Boolean = withContext(Dispatchers.IO) {
        if (isUsable(modelFile)) return@withContext true
        copyFromAssets()
        isUsable(modelFile)
    }

    override suspend fun downloadModel() = withContext(Dispatchers.IO) {
        if (checkIfModelAvailable()) {
            progress.value = null
            return@withContext
        }
        progress.value = 0f
        val temp = File(appContext.filesDir, "${EmbeddingModels.FILE_NAME}.part")
        try {
            val connection = open(EmbeddingModels.DOWNLOAD_URL)
            connection.inputStream.use { input ->
                temp.outputStream().use { output ->
                    val total = connection.contentLengthLong
                    val buffer = ByteArray(DEFAULT_BUFFER)
                    var read = input.read(buffer)
                    var downloaded = 0L
                    while (read >= 0) {
                        output.write(buffer, 0, read)
                        downloaded += read
                        if (total > 0L) {
                            progress.value = (downloaded.toFloat() / total.toFloat()).coerceIn(0f, 1f)
                        } else {
                            progress.value = -1f
                        }
                        read = input.read(buffer)
                    }
                }
            }
            connection.disconnect()
            if (!temp.renameTo(modelFile) || !isUsable(modelFile)) {
                temp.delete()
                modelFile.delete()
                error("The on-device model download did not finish.")
            }
            progress.value = 1f
        } catch (error: Exception) {
            temp.delete()
            throw error
        } finally {
            progress.value = null
        }
    }

    override suspend fun modelFilePath(): String = withContext(Dispatchers.IO) {
        check(checkIfModelAvailable()) { "Download the on-device model before searching." }
        modelFile.absolutePath
    }

    override suspend fun deleteModel() = withContext(Dispatchers.IO) {
        modelFile.delete()
        progress.value = null
    }

    private fun copyFromAssets() {
        val assetNames = appContext.assets.list("")?.toSet().orEmpty()
        if (EmbeddingModels.FILE_NAME !in assetNames) return
        appContext.assets.open(EmbeddingModels.FILE_NAME).use { input ->
            modelFile.outputStream().use { output -> input.copyTo(output) }
        }
    }

    private fun open(url: String): HttpURLConnection {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = true
            connectTimeout = 30_000
            readTimeout = 60_000
            setRequestProperty("User-Agent", "AICommerce/1.0")
        }
        connection.connect()
        if (connection.responseCode !in 200..299) {
            connection.disconnect()
            error("Model download failed (${connection.responseCode}).")
        }
        return connection
    }

    private fun isUsable(file: File): Boolean = file.exists() && file.length() > MIN_MODEL_BYTES

    private companion object {
        const val MIN_MODEL_BYTES = 10_000_000L
        const val DEFAULT_BUFFER = 16 * 1024
    }
}
