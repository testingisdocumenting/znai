# Table Data

Your business logic tests can greatly improve maintainability of your documentation.
Example below test what account types are allowed to perform *Trading Activities*.

:include-file: com/twosigma/testing/documentation/TestToDocExample.groovy {lang: "java"}

As part of validation of account types test also captures data that was used for testing. 
We can refer this data later in our documentation by using [include-table](../features/tables) plugin

    :include-table: account-rules.json
     
Your test data and user facing documentation is now linked.

:include-table: account-rules.json 