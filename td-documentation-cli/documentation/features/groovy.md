# Method Body

When you need to extract a specific method body use `include-groovy` plugin.

Consider a file

:include-groovy: HelloWorld.groovy

Specifying a method name to extract only its body. 

    :include-groovy: HelloWorld.groovy {entry: "should calculate risk based on epsilon", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted. 

:include-groovy: HelloWorld.groovy {entry: "should calculate risk based on epsilon", bodyOnly: true}

