# Information Layout

The way your information is laid out affects how easy it is to comprehend given material.
Some content benefits from being formatted side-by-side. Some examples are:
* Before-and-after transition
* Action and its result
* Input and output

# Definition

To define columns layout use a `fenced` block plugin with the `columns` keyword:
 
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

Size can be specified for a column as a `portion`. By default both portions are assigned a value of `10`. 
If you specify `portion` to be `3` for left column it will occupy `0.3` of the space.

    ```columns {left: {portion: 3}}
    left: 
    this content goes to the left
    
    and can span multiple lines
    right: this content goes to the right
    
    and can span multiple lines
    ```


```columns {left: {portion: 3}}
left: 
this content goes to the left

and can span multiple lines
right: this content goes to the right

and can span multiple lines
```

# Border

You can add a border to your columns.

    ```columns {left: {portion: 3}, border: true}
    left: **Argument Name**
    right: Argument description and what argument is for
    ```
    ```columns {left: {portion: 3}, border: true}
    left: **Another Name**
    right: Argument description and what argument is for
    ```

```columns {left: {portion: 3}, border: true}
left: **Argument Name**
right: Argument description and what argument is for
```
```columns {left: {portion: 3}, border: true}
left: **Another Name**
right: Argument description and what argument is for
```

# Alignment

Specify text alignment using `align`.
    
    ```columns {left: {portion: 3, align: "right"}, border: true}
    left: **Argument Name**
    
    *optional*
    right: Argument description and what argument is for
    ```

```columns {left: {portion: 3, align: "right"}, border: true}
left: **Argument Name**

*optional*

right: Argument description and what argument is for
```
