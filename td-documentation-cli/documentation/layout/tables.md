# CSV

There is a directive to display table data like the one below. 

    :include-table: table.csv 
    
Instead of aligning tables using one of the markdown extensions you can use your `CSV` editor of choice.

So the following `CSV` file

:include-file: table.csv

renders as this table

:include-table: table.csv

# JSON

Same directive can be used to display data from a `JSON` file representing a table data.

    :include-table: table.json 

So the following `JSON` file

:include-file: table.json

renders as this table

:include-table: table.json

# Alignment and Width

By default column width is auto calculated to fit the values inside. 
But you can control the width with the extra options 

    :include-table: table.csv {Price: {width: 200}, "Description": {width: 400}}

:include-table: table.csv {Price: {width: 200}, "Description": {width: 400}}

And if you need to change a column alignment from default *left* to *right* just add `align` property
    
    :include-table: table.csv {Price: {width: 200, align: "right"}}

:include-table: table.csv {Price: {width: 200, align: "right"}}

Or for `JSON` data

    :include-table: table.json {Price: {width: 100, align: "right"}}

:include-table: table.json {Price: {width: 100, align: "right"}}

# Markup in Cells

You can use markup syntax within table file  

:include-file: table-markup.csv

:include-table: table-markup.csv 

# Github Flavored Table

    | Github        | Flavored      | Table |
    | ------------- |:-------------:| -----:|
    | col 3 is      | right-aligned | $1600 |
    | col 2 is      | centered      |   $12 |


| Github        | Flavored      | Table  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |


