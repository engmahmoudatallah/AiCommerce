package com.dolphinstudio.aicommerce.core.ai

interface IosLiteRtHost {
    fun embed(
        modelPath: String,
        prefixedText: String,
        onSuccess: (FloatArray) -> Unit,
        onError: (String) -> Unit,
    )

    fun download(
        url: String,
        destination: String,
        onProgress: (Float) -> Unit,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    fun close()
}

object IosLiteRtBridge {
    var host: IosLiteRtHost? = null

    fun register(host: IosLiteRtHost) {
        this.host = host
    }
}
