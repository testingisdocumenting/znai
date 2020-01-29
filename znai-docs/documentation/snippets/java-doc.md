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

# Handling Links

Links defined with `{@link MyClass}` are automatically converted to [Inlined Code](snippets/inlined-code-snippets) and become
`MyClass`. To turn `MyClass` into the link you need to use [Code References](snippets/code-references).

Use the `referencesPath` value to associate `{@link CustomDomain}` with the documentation link:

```markdown-and-result
:include-java-doc: HelloWorld.java {
    referencesPath: "references/javadoc-references-demo.csv"
}
```

:include-file: references/javadoc-references-demo.csv {title: "references/javadoc-references-demo.csv"}

Alternatively you can define [Global References](snippets/code-references#global-references) to turn `MyClass` into link.

# Method Params

    :include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

:include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

# Method Params With References

Use [Code References](snippets/code-references) file to link API params to reference pages.
  
To do that, define references in a csv file. Two columns format: `type-or-variable-name, link`.

:include-file: references/javadoc-references-demo.csv {title: "references/javadoc-references-demo.csv"}

    :include-java-doc-params: HelloWorld.java {
        entry: "importantAction", 
        referencesPath: "references/javadoc-references-demo.csv"}

:include-java-doc-params: HelloWorld.java {
    entry: "importantAction", 
    referencesPath: "references/javadoc-references-demo.csv"}

Parameters are now linked with reference section of the documentation. 

# Enum Entries

Use `include-java-enum-entries` to enumerate entries of a enum from a file.

:include-file: MyEnum.java

    :include-java-enum-entries: MyEnum.java
    
:include-java-enum-entries: MyEnum.java

You can exclude deprecated entries from the list by specifying `excludeDeprecated` parameter.
 
    :include-java-enum-entries: MyEnum.java {excludeDeprecated: true}

:include-java-enum-entries: MyEnum.java {excludeDeprecated: true}