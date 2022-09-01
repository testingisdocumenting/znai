# Method Body

When you need to extract a specific method body, use the `include-java` plugin.

Consider the file below:

:include-java: HelloWorld.java

You can specify a method name to extract its full definition, or display only its body.

    :include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted.

:include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true}

# Callout Comments

Similar to how you [specify comments type](snippets/code-comments#callout-comments) for a regular file,
you can specify `commentsType` option for `include-java`.

    :include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true, commentsType: "inline"}

:include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true, commentsType: "inline"}

# Title

Use `title` property to specify extracted code snippet title

    :include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true, title: "my snippet"}

:include-java: HelloWorld.java {entry: "sampleMethod", bodyOnly: true, title: "my snippet"}

# Anchor

Use `anchorId` to make a code snippet linkable. Hover mouse over title to see a clickable anchor.

    :include-java: HelloWorld.java {
      entry: "sampleMethod",
      bodyOnly: true,
      title: "my snippet",
      anchorId: "my-java-snippet"
    }

:include-java: HelloWorld.java {
  entry: "sampleMethod", 
  bodyOnly: true,
  title: "my snippet", 
  anchorId: "my-java-snippet"
}

# Signature Only

You can also specify a method name and extract only its signature.

    :include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true}

If `signatureOnly` is specified, body will be omitted.

:include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true}

Pass `entrySeparator: "<separator>"` to have a provided line in between entries as a separator.

    :include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true, entrySeparator: "..."}

:include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true, entrySeparator: "..."}

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

To display multiple methods at once pass a list to `entry` parameter

    :include-java: HelloWorld.java {entry: ["createData", "importantAction"]}

This will render:

:include-java: HelloWorld.java {entry: ["createData", "importantAction"]}

List important methods signatures at one place by passing `signatureOnly: true`.

    :include-java: HelloWorld.java {entry: ["createData", "importantAction"], signatureOnly: true}

This will render:

:include-java: HelloWorld.java {entry: ["createData", "importantAction"], signatureOnly: true}

# Multiple Overloads

To list of the overloads of a method, specify method name using the `entry` parameter.

    :include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true}

:include-java: HelloWorld.java {entry: "sampleMethod", signatureOnly: true}

# Multiple Bodies

Pass multiple entries and `bodyOnly` to extract example of usage from unit tests

:include-file: HelloWorldTest.java { autoTitle: true }

    :include-java: HelloWorldTest.java {
      entry: ["exampleOfA", "exampleOfB"],
      bodyOnly: true,
      title: "example of actions"}

:include-java: HelloWorldTest.java {
entry: ["exampleOfA", "exampleOfB"],
bodyOnly: true,
title: "example of actions"}

Pass `entrySeparator: "<separator>"` to have a provided line in between entries as a separator.

```markdown {highlight: "entrySeparator"}
:include-java: HelloWorldTest.java {
  entry: ["exampleOfA", "exampleOfB"],
  entrySeparator: "",
  bodyOnly: true,
  title: "example of actions"}
```

:include-java: HelloWorldTest.java {
entry: ["exampleOfA", "exampleOfB"],
entrySeparator: "",
bodyOnly: true,
title: "example of actions"}

