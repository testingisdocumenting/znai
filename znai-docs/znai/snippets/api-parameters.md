---
title: API Parameters
---

# Inlined CSV

When you are not able to automatically extract an API parameter definition you can use
the `api-parameters` fence plugin to manually document them.  

    ```api-parameters
    firstName, String, description with *markdown* support
    score, Integer, another description line with *markdown* support
    ```

```api-parameters
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

# Nested

Use `parent.children` syntax to define nested objects like this:

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

```api-parameters
sessionId, Integer, session Id
person, Person, person to login with
person.firstName, String, first name of the person
person.lastName, String, last name of the person
roles, List<Role>, list of authorized roles
roles.id, String, role id 
roles.description, String, role description
```

Note: if a parameter name actually contains a period ("."), you can prevent this nesting behavior by putting the parameter name in single quotes, e.g. 'person.firstName'

# Title

Use the `title` parameter to specify a title.
    
    ```api-parameters {title: "Person Definition"}
    firstName, String, description with *markdown* support
    score, Integer, another description line with *markdown* support
    ```

```api-parameters {title: "Person Definition"}
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```

# External JSON File

Instead of hardcoding your parameters inside markdown files, you can specify an external JSON file.
JSON could be generated based on the data you have. Some of the examples:
* build time annotation processor
* test time command line parameters generation

:include-json: api-parameters.json {title: "api-parameters.json"}

Given the above file, use

    :include-api-parameters: api-parameters.json {title: "Person Definition"}

to display it as API Parameters
    
:include-api-parameters: api-parameters.json {title: "Person Definition"}
    
Note: `description` field in JSON file is treated as Markdown
 
