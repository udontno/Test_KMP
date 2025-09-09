package org.ud.pro

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.example.hevsocks5tunnel.hev_socks5_tunnel_quit
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
fun MainViewController() = ComposeUIViewController {
    LaunchedEffect(Unit) {
        hev_socks5_tunnel_quit()
    }
    App()
}