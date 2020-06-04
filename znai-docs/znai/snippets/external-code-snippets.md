# Embedding Content

To reduce documentation maintenance burden avoid copy and paste of code snippets.
Embed content by referencing existing files using the `:include-file:` plugin instead.  

    :include-file: file-name.js
    
This `include-` syntax will appear throughout the documentation and represents a family of custom Markdown extensions. 

:include-file: file-name.js

The file will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* all lookup paths listed in a `lookup-paths` file

# Title

    :include-file: file-name.js {title: "ES6 class"} 

Use the `title` property to specify a title.

:include-file: file-name.js {title: "ES6 class"} 

# Wide Code

Use the `wide` option to stretch wide code to occupy as much horizontal screen real estate as possible.  

    :include-file: WideCode.java {wide: true}
    
:include-file: WideCode.java {wide: true}

Without the `wide` option code will be aligned with the rest of the text and users can use scrollbars.   

:include-file: WideCode.java

Note: Good placement of a *Wide Code* element is at the end of a page or a section to show the full version of a code sample.

# Read More

If you have a file with large code snippet and you want to initially display only a small fraction use `readMore` 
option with an **optional** `readMoreVisibleLines` option to specify a number of initial lines displayed (default is 8).

    :include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3}
    
:include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3} 

# Highlights

Use the `highlight` option to bring readers attention to the important lines.

    :include-file: file-name.js {highlight: "export"}

:include-file: file-name.js {highlight: "export"}

It is recommended to pass a substring, but you can pass a line idx (starts from 0). 
Additionally you can combine two approaches and pass a list of things to highlight. 

    :include-file: file-name.js {highlight: ["export", 1]}

:include-file: file-name.js {highlight: ["export", 1]}

Note: Order of lines to highlight is reflected during presentation mode

Use the `highlightPath` option to highlight lines specified in a separate file. 

    :include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}
    
:include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}

:include-file: lines-to-highlight.txt {title: "lines-to-highlight.txt"}

# Limit

Use `startLine`, `endLine` to limit the included content.

If you have a file with embedded examples, you can use limit function to extract small samples by using marker lines.

:include-file: python-examples.py {title: "file with examples"} 

    :include-file: python-examples.py {startLine: "example: book trade", endLine: "example-end"}
     
:include-file: python-examples.py {title: "extracted example", startLine: "example: book trade", endLine: "example-end"}

Note: Lines match doesn't have to be exact, `contains` is used.
 
By default `startLine` and `endLine` are included in the rendered result. Use `excludeStartEnd: true` to remove markers.   

    :include-file: python-examples.py { 
        startLine: "example: book trade",
        endLine: "example-end",
        excludeStartEnd: true }

:include-file: python-examples.py {
    title: "extracted example", 
    startLine: "example: book trade",
    endLine: "example-end",
    excludeStartEnd: true }
    
Use `includeRegexp` to include only lines matching regexp(s).

    :include-file: python-examples.py {includeRegexp: "import"}
    :include-file: python-examples.py {includeRegexp: ["import"]}
    
:include-file: python-examples.py { includeRegexp: ["import"], title: "include by regexp" }

Use `excludeRegexp` to exclude lines matching regexp(s).

    :include-file: python-examples.py {excludeRegexp: "# example"}
    :include-file: python-examples.py {excludeRegexp: ["# example"]}
    
:include-file: python-examples.py { excludeRegexp: ["# example"], title: "exclude by regexp" }

# Callout Comments

If you already have comments inside your code it would be non effecient to repeat them inside documentation. 
Instead comments can be automatically extracted and presented as part of the text

Given file with inlined comments

:include-file: file-name-with-comments.js

By specifying `commentsType` 
    
    :include-file: file-name-with-comments.js {commentsType: "inline"}

It will be rendered as 

:include-file: file-name-with-comments.js {commentsType: "inline"}

# Spoilers

Set the `spoiler` property to initially hide explanations. It may be useful when teaching.

    :include-file: file-name-with-comments.js {commentsType: "inline", spoiler: true}

Click on the spoiler to reveal the explanations:

:include-file: file-name-with-comments.js {commentsType: "inline", spoiler: true}
