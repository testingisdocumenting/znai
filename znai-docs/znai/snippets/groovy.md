# Method Body

When you need to extract a specific method body use the `include-groovy` plugin.

Consider the following file:

:include-groovy: HelloWorldTest.groovy

Specify a method name to extract it from the file. 

    :include-groovy: HelloWorldTest.groovy {entry: "should calculate risk based on epsilon", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted. 

:include-groovy: HelloWorldTest.groovy {entry: "should calculate risk based on epsilon", bodyOnly: true}

# Multiple Bodies

Pass `list` as `entry` to extract multiple method bodies

```markdown {highlight: "entry"}
:include-groovy: HelloWorldTest.groovy {
  title: "api example",
  entry: ["should calculate risk based on epsilon", "should calculate risk based on epsilon"],
  bodyOnly: true }
```

:include-groovy: HelloWorldTest.groovy {
  title: "api example",
  entry: ["should calculate risk based on epsilon", "should calculate risk based on epsilon"],
  bodyOnly: true }

Pass `entrySeparator: "<separator>"` to have a provided line in between entries as a separator.

```markdown {highlight: "entrySeparator"}
:include-groovy: HelloWorldTest.groovy {
  title: "api example",
  entry: ["should calculate risk based on epsilon", "should calculate risk based on epsilon"],
  entrySeparator: "",
  bodyOnly: true }
```

:include-groovy: HelloWorldTest.groovy {
  title: "api example",
  entry: ["should calculate risk based on epsilon", "should calculate risk based on epsilon"],
  entrySeparator: "",
  bodyOnly: true }

# Overloads

Specify types inside brackets to select an overloaded versions of your methods. 

Types should appear as they do in the file, i.e., if you use the short version of a type, you need to use the short version 
inside the plugin.

:include-groovy: HelloWorld.groovy

    :include-groovy: HelloWorld.groovy {entry: "methodName(List, Map)"}
    :include-groovy: HelloWorld.groovy {entry: "methodName(def,def)"}
    
Note: Generic types are erased and spaces after commas are optional

:include-groovy: HelloWorld.groovy {entry: "methodName(List, Map)"}

Note: `def` type remains `def` and not `Object`

:include-groovy: HelloWorld.groovy {entry: "methodName(def, def)"}

# Class Body

:include-file: HelloWorld.groovy

To extract `class` body use:

    :include-groovy: HelloWorld.groovy {entry: "HelloWorld"}

:include-groovy: HelloWorld.groovy {entry: "HelloWorld"}

Use `bodyOnly` to only display only the body of your class.

    :include-groovy: HelloWorld.groovy {entry: "HelloWorld", bodyOnly: true}

:include-groovy: HelloWorld.groovy {entry: "HelloWorld", bodyOnly: true}

