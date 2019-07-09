---
title: API Parameters
---

# Inlined CSV

When you are not able to automatically extract an API parameters definition you can use
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

Use `parent.children` syntax to define nested objects like this

    ```api-parameters
    sessionId, Integer, session Id
    person, Person, person to login with
    person.firstName, String, first name of the person
    person.lastName, String, last name of the person
    roles, List<Role>, list of authorized roles
    roles.id, String, role id 
    roles.description, String, role description
    ```
    
Note: you still need to explicitly define `root` entry like `person` in the example above

```api-parameters
sessionId, Integer, session Id
person, Person, person to login with
person.firstName, String, first name of the person
person.lastName, String, last name of the person
roles, List<Role>, list of authorized roles
roles.id, String, role id 
roles.description, String, role description
```

# External JSON file

Instead of hardcoding your parameters inside markdown file, you can specify an external JSON file. 
JSON could be generated based on the data you have. Some of the examples:
* build time annotation processor
* test time command line parameters generation

:include-json: api-parameters.json {title: "api-parameters.json"}

Given the above file, use

    :include-api-parameters: api-parameters.json

to display it as API Parameters
    
:include-api-parameters: api-parameters.json
    
Note: `description` field in JSON file is treated as Markdown
 
