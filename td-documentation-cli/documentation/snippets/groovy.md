# Method Body

When you need to extract a specific method body use `include-groovy` plugin.

Consider a file

:include-groovy: HelloWorldTest.groovy

Specifying a method name to extract only its body. 

    :include-groovy: HelloWorldTest.groovy {entry: "should calculate risk based on epsilon", bodyOnly: true}

If `bodyOnly` is specified, signature will be omitted. 

:include-groovy: HelloWorldTest.groovy {entry: "should calculate risk based on epsilon", bodyOnly: true}

# Overloads

Specify types inside brackets to select an overloaded versions of your methods. 

Types should appear as they are in the file. I.e. if you use short version of a type, you need to use the short version 
inside plugin.

:include-groovy: HelloWorld.groovy

    :include-groovy: HelloWorld.groovy {entry: "methodName(List, Map)"}
    :include-groovy: HelloWorld.groovy {entry: "methodName(def,def)"}
    
Note: Generic types are erased and spaces after commas are optional

:include-groovy: HelloWorld.groovy {entry: "methodName(List, Map)"}

Note: `def` type remains `def` and not `Object`

:include-groovy: HelloWorld.groovy {entry: "methodName(def, def)"}

# Class Body

:include-file: HelloWorld.groovy

To extract `class` body use

    :include-groovy: HelloWorld.groovy {entry: "HelloWorld"}

:include-groovy: HelloWorld.groovy {entry: "HelloWorld"}

Use `bodyOnly` to only display only the body of your class

    :include-groovy: HelloWorld.groovy {entry: "HelloWorld", bodyOnly: true}

:include-groovy: HelloWorld.groovy {entry: "HelloWorld", bodyOnly: true}

