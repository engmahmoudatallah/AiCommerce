package com.dolphinstudio.aicommerce

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform