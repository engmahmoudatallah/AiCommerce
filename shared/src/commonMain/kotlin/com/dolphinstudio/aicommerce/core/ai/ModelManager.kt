package com.dolphinstudio.aicommerce.core.ai

import kotlinx.coroutines.flow.StateFlow

interface ModelManager {
    val downloadProgress: StateFlow<Float?>

    suspend fun checkIfModelAvailable(): Boolean

    suspend fun downloadModel()

    suspend fun modelFilePath(): String

    suspend fun deleteModel()
}
