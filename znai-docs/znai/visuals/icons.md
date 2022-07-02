# Feather Icons

Znai has integration with [Feather Icons](https://feathericons.com/).
Icons can be included using the *inlined code plugin*.

    `:icon: cloud`
    
The result will appear in-lined in the current text `:icon: cloud`

Head over to [https://feathericons.com/](https://feathericons.com/) to find the icon id.

# Bullet Points

Icons can also be used as bullet points replacement:
    
    * `:icon: time` Time is important
    * `:icon: settings` Setup must be easy

* `:icon: time` Time is important
* `:icon: settings` Setup must be easy

# Colors

## Stroke

Use `stroke` parameter to set outline color.

```
* `:icon: time {stroke: 'green'}` stroke: `green`
* `:icon: cloud {stroke: 'blue'}` stroke: `blue`
* `:icon: settings {stroke: 'yellow'}` stroke: `yellow`
* `:icon: x-octagon {stroke: 'red'}` stroke: `red`
```

* `:icon: time {stroke: 'green'}` stroke: `green`
* `:icon: cloud {stroke: 'blue'}` stroke: `blue`
* `:icon: settings {stroke: 'yellow'}` stroke: `yellow`
* `:icon: x-octagon {stroke: 'red'}` stroke: `red`

## Fill

Use `stroke` parameter to set fill color.

* `:icon: x-octagon {fill: 'red'}` fill: `red`
* `:icon: x-octagon {fill: 'red', stroke: 'black'}` fill: `red`, stroke: 'black'

Note: default stroke color is a text color, which may not look good in Dark theme. Use `black` color explicitly if you want
the stroke color to be black in both Light and Dark themes. 

# Inside Tables

Znai provides a shortcut to use icons inside [Tables](layout/tables).

:include-file: layout/table/table-with-shortcuts.csv {title: "table.csv"}

:include-file: layout/table/table-mapping.csv {title: "mapping.csv"}

    :include-table: table.csv {mappingPath: "mapping.csv"}

:include-table: layout/table/table-with-shortcuts.csv {mappingPath: "layout/table/table-mapping.csv"}
