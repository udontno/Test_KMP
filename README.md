# KMP项目-基础使用

## cocoapods插件使用-pod库依赖(branch:use_pod)
- 线上pod仓库
- 本地pod仓库

## spmForKmp插件使用-pod库依赖(branch:use_spmForKmp)
- 线上framework仓库
- 本地framework仓库

## 库型xcframework库依赖(branch:dependence_library_xcframework)
使用spm4kmp插件无法处理基于库的.xcframework（static library + headers + modulemap的结构），需要手动添加def文件定义库进行依赖
1. 下载对应的.xcframework
2. 添加def文件
3. 在gradle中指定这个def文件定义出这个库