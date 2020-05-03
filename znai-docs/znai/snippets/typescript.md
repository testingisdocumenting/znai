# Parsing 

`Znai` uses typescript `nodejs` module to parse TypeScript. Use the `typescript` plugin to include parsed information.

# Properties

:include-file: typescript/Customer.ts {title: 'typescript/Customer.ts'}

Use the `propertiesOf` parameter to list properties of a specified class.

    :include-typescript: typescript/Customer.ts {propertiesOf: "Customer"}
    
:include-typescript: typescript/Customer.ts {propertiesOf: "Customer"}

# JSX Elements

:include-file: typescript/demo.tsx {title: 'demo.tsx'}

Use the `jsxElementsFrom` to extract `JSX` elements from a function. 

    :include-typescript: typescript/demo.tsx {jsxElementsFrom: "buttonsDemo"}
    
:include-typescript: typescript/demo.tsx {jsxElementsFrom: "buttonsDemo"}

