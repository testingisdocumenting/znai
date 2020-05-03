# Table Data

Your business logic tests can greatly improve maintainability of your documentation.
The example below tests what account types are allowed to perform *Trading Activities*.

:include-file: org/testingisdocumenting/testing/examples/TestToDocExample.groovy {lang: "java"}

As part of validation of account types, the test also captures data that was used for testing. 
We can refer to this data later in our documentation by using the [include-table](layout/tables) plugin

    :include-table: account-rules.json
     
Your test data and user-facing documentation is now linked.

:include-table: account-rules.json 
