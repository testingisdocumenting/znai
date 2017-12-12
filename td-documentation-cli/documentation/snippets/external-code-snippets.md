# Existing File

To boost documentation maintainability you can refer text and code snippets from already existing files.

    :include-file: file-name.js
    
Include-dash family is our custom extension to markdown to support various scenarios. 
    
:include-file: file-name.js

File will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* all lookup paths listed in a special file

# Title

    :include-file: file-name.js {title: "ES6 class"} 

Use `title` property to specify a title.

:include-file: file-name.js {title: "ES6 class"} 

# Wide Code

Use `wide` option to stretch wide code to occupy as much real estate as possible.  

    :include-file: WideCode.java {wide: true}
    
:include-file: WideCode.java {wide: true}

Without `wide` option code will be aligned with the rest of the text and users can use scrollbars.   

:include-file: WideCode.java

Note: Good placement of a *Wide Code* is at the end of a page or a section to show the full version of a code sample.

# Read More

If you have a file with large code snippet and you want to initially display only a small fraction use `readMore` 
option with an **optional** `readMoreVisibleLines` option to specify a number of initial lines displayed (default is 8).

    :include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3}
    
:include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3} 

# Highlights

Use `highlight` option to bring readers attention to the important lines.

    :include-file: file-name.js {highlight: "export"}

:include-file: file-name.js {highlight: "export"}

It is recommended to pass a substring, but you can pass a line idx (starts from 0). 
Additionally you can combine two approaches and pass a list of things to highlight. 

    :include-file: file-name.js {highlight: ["export", 1]}

:include-file: file-name.js {highlight: ["export", 1]}

Note: Order of lines to highlight is reflected during presentation mode 

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

Set `spoiler` property to initially hide explanations. It may be useful when teaching or class.

    :include-file: file-name-with-comments.js {commentsType: "inline", spoiler: true}

Click on the spoiler to reveal the explanations.

:include-file: file-name-with-comments.js {commentsType: "inline", spoiler: true}
