Pod::Spec.new do |s|
  s.name             = "MyLocalXCFramework"
  s.version          = "1.0.0"
  s.summary          = "My local xcframework"
  s.license          = "LICENSE"
  s.source           = { :path => "." }
  s.description  = "test description"
  s.homepage     = "http://EXAMPLE/TestP"
  s.author             = { "Udontno" => "1459892307@qq.com" }

  #  指定本地 xcframework
  s.vendored_frameworks = "FBAudienceNetwork.xcframework"
end
