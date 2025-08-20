//
//  TestP.swift
//  TestIOSApp
//
//  Created by Udontno on 2025/8/13.
//
import Foundation

@objc public class TestP: NSObject {
    @objc public override init() {
        super.init()
    }
    @objc public func testp() {
        print("testp")
    }
}

/**
使用未声明为@objc的swift类，会导致无法生成对应可访问的kotlin类，从而无法在kotlin代码中访问
*/
// public class TestP{
//     public func testp() {
//         print("testp")
//     }
// }

