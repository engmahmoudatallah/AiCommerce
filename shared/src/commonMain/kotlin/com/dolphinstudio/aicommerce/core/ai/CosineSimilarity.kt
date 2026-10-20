package com.dolphinstudio.aicommerce.core.ai

import kotlin.math.sqrt

fun cosineSimilarity(first: FloatArray, second: FloatArray): Float {
    if (first.isEmpty() || first.size != second.size) return 0f
    var dot = 0.0
    var firstNorm = 0.0
    var secondNorm = 0.0
    for (index in first.indices) {
        val a = first[index].toDouble()
        val b = second[index].toDouble()
        dot += a * b
        firstNorm += a * a
        secondNorm += b * b
    }
    if (firstNorm == 0.0 || secondNorm == 0.0) return 0f
    return (dot / (sqrt(firstNorm) * sqrt(secondNorm))).toFloat()
}
