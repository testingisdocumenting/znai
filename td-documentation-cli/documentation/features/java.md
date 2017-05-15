# Method Body

When you need to extract a specific method body use `include-java` plugin.

Consider a file

:include-java: HelloWorld.java

Specifying a method name to extract only its body. 

    :include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted. 

:include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true}

# Class JavaDoc

Documentation maintenance is one of the main goals of this documentation system. 
In case of Java you may already use `JavaDoc` strings to explain certain concepts of your system.

:include-java: HelloWorld.java

Instead of copy and pasting text between sources, you can refer it inside documentation.

    :include-java-doc: HelloWorld.java
    
*Text bellow is extracted from top class level `JavaDoc`*

:include-java-doc: HelloWorld.java


# Method JavaDoc

Method level `JavaDoc` text can be refereed as well by specifying `entry` parameter 
    
    :include-java-doc: HelloWorld.java {entry: "sampleMethod"}
    
*Text bellow is extracted from `sampleMethod` method `JavaDoc`*

:include-java-doc: HelloWorld.java {entry: "sampleMethod"}

# Method JavaDoc Params

    :include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

:include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

