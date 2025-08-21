package org.ud.pro

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import cocoapods.Base64.MF_Base64Codec
import cocoapods.TestP.TestP
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import cocoapods.AFNetworking.AFNetworkActivityIndicatorManager
import cocoapods.MyLocalXCFramework.FBRewardedVideoAd
// 指定packageName会指定类生成在哪个包名下
import org.ud.pro.rs.ReachabilityVersionString

@OptIn(ExperimentalForeignApi::class)
fun MainViewController() = ComposeUIViewController {
    LaunchedEffect(Unit) {

        // 线上指定git地址库调用
        MF_Base64Codec.base64StringFromData(NSData())

        // 指定名字的pod库，默认会使用最新版本
        AFNetworkActivityIndicatorManager.sharedManager().setEnabled(true)

        // 不支持oc的swift库无法在kotlin中被调用
        ReachabilityVersionString

        // 本地库调用
        val testP = TestP()
        testP.testp()

        FBRewardedVideoAd().loadAd()
    }

    App()
}