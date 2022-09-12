# Information Layout

Use `columns` plugin to render content in two columns:
* Before-and-after transition
* Action and its result
* Input and output
* Result comparison

# Two Columns

To define two columns layout use fenced code block and `left:` and `right:` labels 

`````markdown 
```columns
left: 
this content goes to the left

and can span multiple lines

right: 
this content goes to the right
and can span multiple lines
```
`````

```columns
left: 
this content goes to the left

and can span multiple lines

right: 
this content goes to the right
and can span multiple lines
```

# Three Columns

To define three columns layout use fenced code block and `left:`, `middle:` and `right:` labels

````markdown
```columns
left: 
### Pros {style: "api"}
* Item One

middle: 
### Undecided {style: "api"}
* Item Two
* Item Three

right:
### Cons {style: "api"}
* Item Four
```
````

```columns
left: 
### Pros {style: "api"}
* Item One

middle: 
### Undecided {style: "api"}
* Item Two
* Item Three

right:
### Cons {style: "api"}
* Item Four
```

# Sizes

Size can be specified for a column as a `portion`. By default, both portions are assigned a value of `10`. 
If you specify right column `portion` to be `5`, then left will be left as `10` by default, 
leaving right side to be roughly 33% in size.


```columns {right: {portion: 5}}
left: 
:include-groovy: org/testingisdocumenting/testing/examples/restapi/WebTauRestAPIGroovyTest.groovy {
  title: "WebTau REST API test example",
  entry: "weather",
  bodyOnly: true
}

right:
:include-json: weather-example/response.json {
  title: "weather response example",
  pathsFile: "weather-example/paths.json"
}
```

    ```columns {right: {portion: 5}}
    left: 
    :include-groovy: org/testingisdocumenting/testing/examples/restapi/WebTauRestAPIGroovyTest.groovy {
      title: "WebTau REST API test example",
      entry: "weather",
      bodyOnly: true
    }
    
    right:
    :include-json: weather-example/response.json {
      title: "weather response example",
      pathsFile: "weather-example/paths.json"
    }
    ```
    
# Border

Use `border` to add a border separator between columns

```columns {border: true}
left: 
**Before**

Hello World

right:
**After**

World Of Hellos
```

    ```columns {border: true}
    left: 
    **Before**
    
    Hello World
    
    right:
    **After**
    
    World Of Hellos
    ```

# Alignment

Use `align` to change a column content alignment.

```columns {border: true, left: {align: "right"}}
left: 
**Before**

Hello World

right:
**After**

World Of Hellos
```
    
    ```columns {border: true, left: {align: "right"}}
    left: 
    **Before**
    
    Hello World
    
    right:
    **After**
    
    World Of Hellos
    ```

# Presentation Mode

In Presentation Mode each column content will appear on slide transition