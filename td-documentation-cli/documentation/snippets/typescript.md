# Parsing 

Mdoc uses typescript `nodejs` module to parse typescript. Use `typescript` plugin to include parsed information.
Following file will be used to demonstrate plugin usage.

:include-file: typescript/Customer.ts {title: 'typescript/Customer.ts'}

# Properties

Use `propertiesOf` parameter to list properties of a specified class.

    :include-typescript: typescript/Customer.ts {propertiesOf: "Customer"}
    
:include-typescript: typescript/Customer.ts {propertiesOf: "Customer"}