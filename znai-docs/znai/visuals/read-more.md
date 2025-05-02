# Collapsible Content

To collapse content use `readmore` fence plugin

    ````readmore {title: "Title of the Hidden Details"}
    Information you don't want users to see right away goes here.
    Can use all the markdown inside
    1. item A
    2. item B
        
    ```
    let list = [1, 2, 3]
    let value = 0
    ```
    ````

````readmore {title: "Title of the Hidden Details"}
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
