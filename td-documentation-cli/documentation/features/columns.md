# Information Layout

The way you layout and flow your information affects how easy it is to comprehend given material.
Examples of content that easier to consume side by side:
* Before/After transition
* Action and its result
* input and output

# Definition

To define columns layout use `fenced` block plugin
 
    ```columns
    left: 
    this content goes to the left
    
    and can span multiple lines
    right: this content goes to the right
    
    and can span multiple lines
    ```


```columns
left: 
this content goes to the left

and can span multiple lines
right: this content goes to the right

and can span multiple lines
```

# Sizes

Size can be specified for a column

    ```columns {left: {width: 160}}
    left: 
    this content goes to the left
    
    and can span multiple lines
    right: this content goes to the right
    
    and can span multiple lines
    ```


```columns {left: {width: 160}}
left: 
this content goes to the left

and can span multiple lines
right: this content goes to the right

and can span multiple lines
```

# Border

Size can be specified for a column
    
    ```columns {left: {width: 160}, border: true}
    left: **Argument Name**
    right: Argument description and what argument is for
    ```
    ```columns {left: {width: 160}, border: true}
    left: **Another Name**
    right: Argument description and what argument is for
    ```

```columns {left: {width: 160}, border: true}
left: **Argument Name**
right: Argument description and what argument is for
```
```columns {left: {width: 160}, border: true}
left: **Another Name**
right: Argument description and what argument is for
```

# Alignment

Specify text alignment using `align`
    
    ```columns {left: {width: 180, align: "right"}, border: true}
    left: **Argument Name**
    
    *optional*
    right: Argument description and what argument is for
    ```

```columns {left: {width: 180, align: "right"}, border: true}
left: **Argument Name**

*optional*

right: Argument description and what argument is for
```
