# Method Body

When you need to extract a specific method body use `include-java` plugin.

Consider the file below:

:include-java: HelloWorld.java

You can specify a method name to extract its full definition, or display only its body. 

    :include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted. 

:include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true}

# Callout Comments

Similar to how you [specify comments type](snippets/external-code-snippets#callout-comments) for a regular file,
you can specify `commentsType` option for `include-java`.

    :include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true, commentsType: "inline"}

:include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true, commentsType: "inline"}

# Signature Only

Specifying a method name to extract only its body. 

    :include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true}

If `signatureOnly` is specified, body will be omitted. 

:include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true}

# Overloads

Specify types inside brackets to select an overloaded versions of your methods. 

Types should appear as they are in the file, i.e., if you use the short version of a type, you need to use the short version 
inside the plugin.

    :include-java: HelloWorld.java {entry: "sampleMethod(Map, int, boolean)"}

Note: Generic types are erased and spaces after commas are optional

:include-java: HelloWorld.java {entry: "sampleMethod(Map, int, boolean)"}

# Type Body

To extract `class`, `interface` or `enum` body use:

    :include-java: MyEnum.java {entry: "MyEnum"}
    
:include-java: MyEnum.java {entry: "MyEnum"}

Use `bodyOnly` to only display body of your type.

    :include-java: MyEnum.java {entry: "MyEnum", bodyOnly: true}
    
:include-java: MyEnum.java {entry: "MyEnum", bodyOnly: true}

# Multiple Entries

To display multiple methods at once use `entries` parameter to pass a list of method names.
    
    :include-java: HelloWorld.java {entries: ["sampleMethod", "importantAction"]}

will render:

:include-java: HelloWorld.java {entries: ["sampleMethod", "importantAction"]}

List important methods signatures at one place by passing `signatureOnly: true`.

    :include-java: HelloWorld.java {entries: ["sampleMethod", "importantAction"], signatureOnly: true}

will render: 

:include-java: HelloWorld.java {entries: ["sampleMethod", "importantAction"], signatureOnly: true}

# Class JavaDoc

Documentation maintenance is one of the main goals of this documentation system. 
In case of Java you may already use `JavaDoc` strings to explain certain concepts of your system.

:include-java: HelloWorld.java

Instead of copy and pasting text between sources, you can refer to it inside your documentation.

    :include-java-doc: HelloWorld.java
    
*Text bellow is extracted from top class level `JavaDoc`*

:include-java-doc: HelloWorld.java

# Method JavaDoc

Method level `JavaDoc` text can be referred to as well by specifying the `entry` parameter.
    
    :include-java-doc: HelloWorld.java {entry: "sampleMethod"}
    
*Text bellow is extracted from `sampleMethod` method `JavaDoc`*

:include-java-doc: HelloWorld.java {entry: "sampleMethod"}

# Field JavaDoc 
    
    :include-java-doc: HelloWorld.java {entry: "numberOfStudents"}
    
:include-java-doc: HelloWorld.java {entry: "numberOfStudents"}

# Method JavaDoc Params

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

