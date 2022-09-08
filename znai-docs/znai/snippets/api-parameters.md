---
title: API Parameters
---

# Inlined CSV

Use `api-parameters` fenced code plugin to document API parameters.

    ```api-parameters
    firstName, String, description with *markdown* support
    score, Integer, another description line with *markdown* support
    ```

```api-parameters {anchorPrefix: "inlined_csv"}
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

# Nested

Use `parent.child` syntax to define nested objects like this:

    ```api-parameters
    sessionId, Integer, session Id
    person, Person, person to login with
    person.firstName, String, first name of the person
    person.lastName, String, last name of the person
    roles, List<Role>, list of authorized roles
    roles.id, String, role id 
    roles.description, String, role description
    ```
    
Note: when using this approach it is necessary to explicitly define a `root` entry, such as `person` in this example.

```api-parameters {anchorPrefix: "nested_inlined_csv"}
sessionId, Integer, session Id
person, Person, person to login with
person.firstName, String, first name of the person
person.lastName, String, last name of the person
roles, List<Role>, list of authorized roles
roles.id, String, role id 
roles.description, String, role description
```

Note: if a parameter name actually contains a period ("."), you can prevent this nesting behavior by putting the parameter name in single quotes, e.g. "person.firstName"

# Title

Use the `title` parameter to specify a title.
    
    ```api-parameters {title: "person definition"}
    firstName, String, description with *markdown* support
    score, Integer, another description line with *markdown* support
    ```

```api-parameters {title: "person definition", anchorPrefix: "title"}
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

# Collapsing Parameters

Use `collapsed: true|false` to make `api-parameters` collapsible. 

`````markdown
```api-parameters {title: "address definition", anchorPrefix: "title", collapsed: true}
city, String, city name
zipCode, String, zip code
```
`````

```api-parameters {title: "address definition", anchorPrefix: "title", collapsed: true}
city, String, city name
zipCode, String, zip code
```

Note: `collapsed` requires `title` to be present

# No Gap

Use `noGap: true` to remove top/bottom margins when there are multiple parameter instances in a row.

`````markdown
```api-parameters { title: "person definition", anchorPrefix: "title", collapsed: false, noGap: true }
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

```api-parameters {title: "address definition", anchorPrefix: "title", collapsed: true, noGap: true}
city, String, city name
zipCode, String, zip code
```
`````

```api-parameters { title: "person definition", anchorPrefix: "title", collapsed: false, noGap: true }
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

```api-parameters {title: "address definition", anchorPrefix: "title", collapsed: true, noGap: true}
city, String, city name
zipCode, String, zip code
```


# Size

Use the `small` parameter to render API Parameters using smaller font size and occupying less width

    ```api-parameters {small: true}
    firstName, String, description with *markdown* support
    score, Integer, another description line with *markdown* support
    ```

```api-parameters {small: true, anchorPrefix: "size"}
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

# Multi-line CSV Description

Use quotes `"` to wrap a multiline description. Here is an example of description including multiple lines and 
nested code block example.

    `````api-parameters 
    cores, String, "
    ```
    execute(cores: 10)
    ```
    Specify how many cores to allocate for execution
    "
    
    gpu, Boolean, "
    ```
    execute(cores: 10, gpu: true)
    ```
    Specify whether to use gpu
    "
    `````

`````api-parameters 
cores, String, "
```
execute(cores: 10)
```
Specify how many cores to allocate for execution
"

gpu, Boolean, "
```
execute(cores: 10, gpu: true)
```
Specify whether to use gpu
"
`````

Note: Use larger number of backticks on outside then inside to distinct between `api-parameters` plugin boundaries 
and nested code blocks

# External JSON File

Instead of hard-coding your parameters inside markdown files, you can specify an external JSON file.
JSON could be generated based on the data you have. Some examples:
* build time annotation processor
* test time command line parameters generation

:include-json: api-parameters.json {title: "api-parameters.json"}

Given the above file, use

    :include-api-parameters: api-parameters.json {title: "Person Definition"}

to display it as API Parameters
    
:include-api-parameters: api-parameters.json {title: "Person Definition", anchorPrefix: "json"}
    
Note: `description` field in JSON file is treated as Markdown
 
# Anchors

Each API parameter has an anchor associated with it. You need to hover over parameter name for it to appear.
Use `anchorPrefix` parameter to avoid conflict of anchor ids when using the same API parameter names within a single page:

    ```api-parameters {anchorPrefix: "customPrefix"}
    firstName, String, description with *markdown* support
    score, Integer, another description line with *markdown* support
    ```

```api-parameters {anchorPrefix: "customPrefix"}
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

In the example above `customPrefix` is added to each parameter link.

# Long Parameter Names

Znai hard wraps long parameter names to leave more space to description. 

```api-parameters {anchorPrefix: "customPrefix"}
VERY_LONG_PARAMETER_NAME_WITHOUT_SPACES, String, description with *markdown* support
VERY_LONG_ANOTHER_PARAMETER_NAME_WITHOUT_SPACES, String, another description line with *markdown* support
```

Use `noWrap: true` to remove hard wrap enforcement

    ```api-parameters {anchorPrefix: "customPrefix", noWrap: true}
    VERY_LONG_PARAMETER_NAME_WITHOUT_SPACES, String, description with *markdown* support
    VERY_LONG_ANOTHER_PARAMETER_NAME_WITHOUT_SPACES, String, another description line with *markdown* support
    ```

```api-parameters {anchorPrefix: "customPrefix", noWrap: true}
VERY_LONG_PARAMETER_NAME_WITHOUT_SPACES, String, description with *markdown* support
VERY_LONG_ANOTHER_PARAMETER_NAME_WITHOUT_SPACES, String, another description line with *markdown* support
```

# Wide Mode

Use `wide: true` to use all the available horizontal space

    ```api-parameters {anchorPrefix: "customPrefix", noWrap: true, wide: true}
    VERY_LONG_PARAMETER_NAME_WITHOUT_SPACES, String, "longer line longer line, description with *markdown* support"
    VERY_LONG_ANOTHER_PARAMETER_NAME_WITHOUT_SPACES, String, "longer line longer line, another description 
    line with *markdown* support and few moe lines"
    ```

```api-parameters {anchorPrefix: "customPrefix", noWrap: true, wide: true}
VERY_LONG_PARAMETER_NAME_WITHOUT_SPACES, String, "longer line longer line, description with *markdown* support"
VERY_LONG_ANOTHER_PARAMETER_NAME_WITHOUT_SPACES, String, "longer line longer line, another description 
line with *markdown* support and few moe lines"
```
