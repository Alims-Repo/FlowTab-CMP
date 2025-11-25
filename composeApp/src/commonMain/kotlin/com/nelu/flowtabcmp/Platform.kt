package com.nelu.flowtabcmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform