# CSV

Instead of aligning tables using one of the standard Markdown extensions, you can use your `CSV` editor of choice.

    :include-table: table.csv 
    

In this way, the following `CSV` file...

:include-file: table/table.csv

...will render like so:

:include-table: table/table.csv

# JSON

A similar extension can be used to display data from a `JSON` file representing tabular data.

    :include-table: table.json 

So the following `JSON` file...

:include-file: table/table.json

...will render like so:

:include-table: table/table.json

# Title

Use `title` parameter to set a table title.

    :include-table: table.json {title: "Monthly Report"}
    
:include-table: table/table.json {title: "Monthly Report"}

# Anchor

When you specify a title, hover mouse over it to see a clickable anchor.

Use `anchorId` to override auto generated identifier.

    :include-table: table/table.json {title: "Monthly Report", anchorId: "my-super-table"}

:include-table: table/table.json {title: "Monthly Report", anchorId: "my-super-table"}

# Collapse

Use `collapsed: true|false` to make image collapsible.
Note: `title` option is required

```markdown {highlight: "collapsed"}
:include-table: table/table.json {
  title: "Monthly Report",
  collapsed: true
}
```

:include-table: table/table.json {title: "Monthly Report", collapsed: true}

# No Gap

Use `noGap: true` to remove top/bottom margins when there are multiple tables in a row.

```markdown {highlight: "noGap"}
:include-table: table/table.json {
  title: "Monthly Report",
  collapsed: true,
  noGap: true,
}

:include-table: table/table.json {
  title: "Weekly Report",
  collapsed: false,
  noGap: true,
}
```

:include-table: table/table.json {
  title: "Monthly Report",
  collapsed: true,
  noGap: true,
}

:include-table: table/table.json {
  title: "Weekly Report",
  collapsed: false,
  noGap: true,
}

# Highlight

Use `highlightRow` to highlight a row, or a set of rows by their index

    :include-table: table/table.json {title: "Highlight multiple rows", highlightRow: [0, 2]}

:include-table: table/table.json {title: "Highlight multiple rows", highlightRow: [0, 2]}

    :include-table: table/table.json {title: "Highlight single row", highlightRow: 1}

:include-table: table/table.json {title: "Highlight single row", highlightRow: 1}


# Arrange and Remove Columns

To change the order of columns or to filter out certain columns, specify the `columns` parameter.

    :include-table: table.csv {columns: ["Description", "Price"]}
    
:include-table: table/table.csv {columns: ["Description", "Price"]}

# Filter Rows

Use `:identifier: includeRowsRegexp {validationPath: "org/testingisdocumenting/znai/extensions/table/TablePluginParams.java"}` 
to include only rows that match a regular expression

    :include-table: table/table.csv {includeRowsRegexp: ["table", "cha.."]}

:include-table: table/table.csv {includeRowsRegexp: ["table", "cha.."]}

Use `:identifier: excludeRowsRegexp {validationPath: "org/testingisdocumenting/znai/extensions/table/TablePluginParams.java"}`
to exclude rows that match a regular expression

    :include-table: table/table.csv {includeRowsRegexp: ["table", "cha.."], excludeRowsRegexp: "short"}

:include-table: table/table.csv {includeRowsRegexp: ["table", "cha.."], excludeRowsRegexp: "short"}

# Width

By default, columns width is auto-calculated to fit the values inside.
Use `width` to control the width of a column.

    :include-table: table.csv {Price: {width: 200}, "Description": {width: 400}}

:include-table: table/table.csv {Price: {width: 200}, "Description": {width: 400}}

Use percentage value to change column size relative to the page content width. 

    :include-table: table/table.csv {Price: {width: "50%"}, "Description": {width: "30%"}}

:include-table: table/table.csv {Price: {width: "50%"}, "Description": {width: "30%"}}

Note: total percentage of all columns can go above 100%, in which case a scroll bar will be used to fit the content 

    :include-table: table/table.csv {Price: {width: "50%"}, "Description": {width: "60%"}}

:include-table: table/table.csv {Price: {width: "50%"}, "Description": {width: "60%"}}

# Min Width

Use `minColumnWidth` to set min width for all table columns

    :include-table: table/table.csv {minColumnWidth: 400, "Description": {width: 500}}

:include-table: table/table.csv {minColumnWidth: 400, "Description": {width: 500}}

# Wide Mode

Use `wide: true` to make table occupy all available width

    :include-table: table/table.csv {wide: true, minColumnWidth: 400, title: "My Items"}

:include-table: table/table-many-columns.csv {wide: true, minColumnWidth: 400, title: "My Items"}

# Text Alignment

Use `align` to change a column text alignment.
    
    :include-table: table.csv {Price: {width: 200, align: "right"}}

:include-table: table/table.csv {Price: {width: 200, align: "right"}}

`JSON` data example:

    :include-table: table.json {Price: {width: 100, align: "right"}}

:include-table: table/table.json {Price: {width: 100, align: "right"}}

# Mapping

Use `mappingPath` to provide a way to override table values using mapping table.

:include-file: table/table-with-shortcuts.csv {title: "table.csv"}

:include-file: table/table-mapping.csv {title: "mapping.csv"}

    :include-table: table.csv {mappingPath: "mapping.csv"}

:include-table: table/table-with-shortcuts.csv {mappingPath: "table/table-mapping.csv"}

Note: We used [Icons](visuals/icons) in this example, but you can use any plugins or any text you want.

# Inlined

    ```table {title: "my table", Price: {width: 100, align: "right"}}
    Account, Price, Description
    #12BGD3, 100, custom table with a long attachment
    #12BGD3, 150, chair
    #91AGB1, 10, lunch
    ```
 
```table {title: "my table", Price: {width: 100, align: "right"}}
Account, Price, Description
#12BGD3, 100, custom table with a long attachment
#12BGD3, 150, chair
#91AGB1, 10, lunch
```

# Multiline Content

Use quotes to provide multi line markdown content such as bullet points and code snippets

:include-file: table-multiline.csv
:include-table: table-multiline.csv

# Markdown in Cells

You can use Markdown table syntax within your table file.

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


