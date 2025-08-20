package org.ud.pro

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform