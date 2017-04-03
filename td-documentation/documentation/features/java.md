# Method Body

When you need to extract a specific method body use `include-java` plugin.

Consider a file

:include-java: simple.java

Specifying a method name to extract only its body. 

    :include-java: simple.java {entry: "sampleMethod", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted. 

:include-java: simple.java {entry: "sampleMethod", bodyOnly: true}
