import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    cocoapods {
        version = "2.0"
        summary = "Kotlin sample project with CocoaPods dependencies"
        homepage = "https://github.com/Kotlin/kotlin-with-cocoapods-sample"
        ios.deploymentTarget = "16.0"

        framework {
            // 必须属性
            // 配置框架名称. 'frameworkName' 属性已废弃, 请改为使用这个属性
            baseName = "ComposeApp"
        }

        // 将自定义的 Xcode 配置对应到 NativeBuildType
//        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
//        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

        // 线上pod库
        pod("AFNetworking")// 不指定默认最新版本

        pod("ReachabilitySwift") {
            version = "5.2.4" // 指定版本号
//            version = "~> 5.2.0" // 指定版本范围
            moduleName =
                "Reachability"// 不指定时默认与 Pod 名相同——需要去查看库里面的podspec文件，此处的podspec声明：s.module_name = 'Reachability'，由于和库名称不同所以需要手动指定
            packageName = "org.ud.pro.rs"// 指定 Kotlin 包名（生成的 API 会放在这个包里），默认包名是根据 Pod 名生成的

            // 只链接 Pod，而不生成 cinterop 绑定，避免编译时间过长
            // 设为 true 时，Kotlin 代码里 不能直接调用 Pod 的 API，只能在 iOS 原生（Swift/ObjC）侧用
//            linkOnly = true

            // 以下比较少用
            // 作用：指定头文件路径（cinterop 需要知道 .h 文件的位置）一般不用指定headers
            //  一般用于 Pod 没有标准模块化头文件 时，手动指定头文件目录
            //  CocoaPods 会在安装 Pod 时自动为其生成 modulemap 文件，前提是 Pod 的 podspec 文件中设置了 DEFINES_MODULE = true（这个参数在swift Pod时默认为true，在ObjectC Pod时默认为false）
//            headers = "src/nativeInterop/cinterop/legacyHeaders"

            // 作用：声明当前 Pod 的 cinterop 绑定依赖
//            interopBindingDependencies += listOf()
        }

        pod("Base64") {
            source = git("https://github.com/ekscrypto/Base64.git") {
                // TODO 如果三个指定的内容正确但不匹配，不报错，能正常访问../commit内容错误，tag内容正确，不报错，能正常访问
//                branch = "master" // 指定分支
//                commit = "b8bcb01" // 指定哪一次提交
                tag = "1.2.2" // 指定tag
            }
        }

        // 本地pod库
        pod("TestP") {
            // 创建本地Pod库方式参考：https://www.jianshu.com/p/9183cded6b2e
            source = path(rootDir.path + "/TestP")

            // 存在@objc的swift代码文件时必须添加如下编译器参数，否则会报错：
            /*
            :composeApp:iosArm64Main: cinterop file: KMPProject/composeApp/build/libs/composeApp-iosArm64Cinterop-TestPMain.klib does not exist
             */
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

    }


    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "org.ud.pro"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.ud.pro"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.ud.pro.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.ud.pro"
            packageVersion = "1.0.0"
        }
    }
}
