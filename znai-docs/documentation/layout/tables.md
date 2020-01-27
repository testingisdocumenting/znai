# CSV

Instead of aligning tables using one of the standard Markdown extensions, you can use your `CSV` editor of choice.

    :include-table: table.csv 
    

In this way, the following `CSV` file...

:include-file: table.csv

...will render like so:

:include-table: table.csv

# JSON

A similar extension can be used to display data from a `JSON` file representing tabular data.

    :include-table: table.json 

So the following `JSON` file...

:include-file: table.json

...will render like so:

:include-table: table.json

# Title

Use `title` parameter to set table title.

    :include-table: table.json {title: "Monthly Report"}
    
:include-table: table.json {title: "Monthly Report"}

# Arrange and Filter

To change the order of columns or filter out certain columns, specify the `columns` parameter.

    :include-table: table.csv {columns: ["Description", "Price"]}
    
:include-table: table.csv {columns: ["Description", "Price"]}

# Alignment and Width

By default column width is auto-calculated to fit the values inside. 
But you can control the width with the extra options.

    :include-table: table.csv {Price: {width: 200}, "Description": {width: 400}}

:include-table: table.csv {Price: {width: 200}, "Description": {width: 400}}

And if you need to change a column alignment from default *left* to *right* just add the `align` property.
    
    :include-table: table.csv {Price: {width: 200, align: "right"}}

:include-table: table.csv {Price: {width: 200, align: "right"}}

You can do the same for `JSON` data:

    :include-table: table.json {Price: {width: 100, align: "right"}}

:include-table: table.json {Price: {width: 100, align: "right"}}

# Markdown in Cells

You can use markup syntax within table file.

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


