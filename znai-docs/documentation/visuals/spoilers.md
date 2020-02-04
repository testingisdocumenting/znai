# Hidden Content

To hide content use the `spoiler` fence plugin

    ````spoiler 
    Information you don't want users to see right away goes here.
    Can use all the markdown inside
    1. item A
    2. item B
        
    ```
    let list = [1, 2, 3]
    let value = 0
    ```
    ````
     
````spoiler 
Information you don't want users to see right away goes here.
Can use all the markdown inside
1. item A
2. item B
    
```
let list = [1, 2, 3]
let value = 0
```
````

Note: Four backticks were used instead of the usual three. This is done so one fence block can include 
another block (code snippet) without confusion. It can be any number of backticks greater than three.

# Hidden Content Title

Use the `title` parameter to set a custom title for your spoiler.

    ```spoiler {title: "Are you ready to see what's inside?"}
    ...
    ```

```spoiler {title: "Are you ready to see what's inside?"}
1. Line One
2. Line Two
3. Line Three
```