# Class Level

Documentation maintenance is one of the main goals of this documentation system.
In the case of Java you may already use `JavaDoc` strings to explain certain concepts of your system.

:include-java: HelloWorld.java

Instead of copy-and-pasting text between sources, you can refer to it inside your documentation.

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

# Inner Classes

Use `NestedName.entityName` to disambiguate a field or a method name

:include-java: HelloWorldWithInner.java {autoTitle: true}

    :include-java-doc: HelloWorldWithInner.java {entry: "Nested.importantScore"}

:include-java-doc: HelloWorldWithInner.java {entry: "Nested.importantScore"}


# Handling Links

Links defined with `{@link MyClass}` are automatically converted to [Inlined Code](snippets/inlined-code-snippets) and become
`MyClass`. To turn `MyClass` into a link you need to use [Code References](snippets/code-references).

Use the `referencesPath` value to associate `{@link CustomDomain}` with the documentation link:

```markdown-and-result
:include-java-doc: HelloWorld.java {
    referencesPath: "references/javadoc-references-demo.csv"
}
```

:include-file: references/javadoc-references-demo.csv {title: "references/javadoc-references-demo.csv"}

Alternatively you can define [Global References](snippets/code-references#global-references) to turn `MyClass` into a link.

# Method Params

    :include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

:include-java-doc-params: HelloWorld.java {entry: "sampleMethod"}

Use the `title` parameter to specify a title.

    :include-java-doc-params: HelloWorld.java {entry: "importantAction", title: "Trading Required Parameters"}

:include-java-doc-params: HelloWorld.java {entry: "importantAction", title: "Trading Required Parameters"}


# Method Params With References

Use the [Code References](snippets/code-references) file to link method parameters to reference pages.

To do that, define references in a CSV file, using a two column format: `type-or-variable-name, link`.

:include-file: references/javadoc-references-demo.csv {title: "references/javadoc-references-demo.csv"}

    :include-java-doc-params: HelloWorld.java {
        entry: "importantAction", 
        title: "Trading Required Parameters",
        referencesPath: "references/javadoc-references-demo.csv"}

:include-java-doc-params: HelloWorld.java {
  entry: "importantAction",
  title: "Trading Required Parameters",
  referencesPath: "references/javadoc-references-demo.csv"
}

Parameters are now linked with a reference section for the documentation.

# Enum Entries

Use `include-java-enum-entries` to enumerate entries of a enum from a file.

:include-file: MyEnum.java {title: "MyEnum.java"}

    :include-java-enum-entries: MyEnum.java

:include-java-enum-entries: MyEnum.java

You can exclude deprecated entries from the list by setting the `excludeDeprecated` parameter.

    :include-java-enum-entries: MyEnum.java {excludeDeprecated: true}

:include-java-enum-entries: MyEnum.java {excludeDeprecated: true}

Use the `title` parameter to specify a title.

    :include-java-enum-entries: TransactionTypes.java {title: "Transaction Types"}

:include-java-enum-entries: TransactionTypes.java {title: "Transaction Types"}


# Enum Entries With References

Use a [Code References](snippets/code-references) file to link enum entries to reference pages.

To do that, define references in a CSV file, using a two column format: `enum-name, link`.

:include-file: references/javadoc-references-demo.csv {title: "references/javadoc-references-demo.csv"}

:include-file: TransactionTypes.java {title: "TransactionTypes.java"}

    :include-java-enum-entries: TransactionTypes.java {
        title: "Transaction Types",
        referencesPath: "references/javadoc-references-demo.csv"
    }

:include-java-enum-entries: TransactionTypes.java {referencesPath: "references/javadoc-references-demo.csv", title: "Transaction Types"}

Enums are now linked with a reference section for the documentation.

Note: you can reuse the same CSV file for Enums, Parameters, Code Snippets. Alternatively, you can use
[Global References](snippets/code-references#global-references).
