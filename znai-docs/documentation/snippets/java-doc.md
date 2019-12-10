# Class Level

Documentation maintenance is one of the main goals of this documentation system. 
In case of Java you may already use `JavaDoc` strings to explain certain concepts of your system.

:include-java: HelloWorld.java

Instead of copy and pasting text between sources, you can refer to it inside your documentation.

    :include-java-doc: HelloWorld.java
    
*Text bellow is extracted from top class level `JavaDoc`*

:include-java-doc: HelloWorld.java

# Method Level

Method level `JavaDoc` text can be referred to as well by specifying the `entry` parameter.
    
    :include-java-doc: HelloWorld.java {entry: "sampleMethod"}
    
*Text bellow is extracted from `sampleMethod` method `JavaDoc`*

:include-java-doc: HelloWorld.java {entry: "sampleMethod"}

# Field Level
    
    :include-java-doc: HelloWorld.java {entry: "numberOfStudents"}
    
:include-java-doc: HelloWorld.java {entry: "numberOfStudents"}

# Method Params

    :include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

:include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

# Enum Entries

Use `include-java-enum-entries` to enumerate entries of a enum from a file.

:include-file: MyEnum.java

    :include-java-enum-entries: MyEnum.java
    
:include-java-enum-entries: MyEnum.java

You can exclude deprecated entries from the list by specifying `excludeDeprecated` parameter.
 
    :include-java-enum-entries: MyEnum.java {excludeDeprecated: true}

:include-java-enum-entries: MyEnum.java {excludeDeprecated: true}