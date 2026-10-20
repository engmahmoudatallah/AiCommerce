package com.dolphinstudio.aicommerce.core.ai

enum class EmbeddingPurpose {
    Query,
    Document,
}

interface EmbeddingEngine {
    val modelId: String

    suspend fun embed(text: String, purpose: EmbeddingPurpose): FloatArray
}

object EmbeddingModels {
    const val GEMMA_TEXT_270M = "embeddinggemma-2-text-270m"
    const val FILE_NAME = "embeddinggemma-2-text-270m.litertlm"
    const val DOWNLOAD_URL =
        "https://huggingface.co/litert-community/embeddinggemma-2-text-270m-litert-lm/resolve/main/embeddinggemma-2-text-270m.litertlm"

    const val QUERY_PREFIX = "task: search query | text: "
    const val DOCUMENT_PREFIX = "task: search result | text: "

    fun prefix(text: String, purpose: EmbeddingPurpose): String {
        val body = text.trim()
        val instruction = if (purpose == EmbeddingPurpose.Query) QUERY_PREFIX else DOCUMENT_PREFIX
        return instruction + body
    }
}
