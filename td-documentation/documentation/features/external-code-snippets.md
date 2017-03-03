# Existing File

To boost documentation maintainability you can refer text and code snippets from already existing files.

    :include-text-file: file-name.js
    
Include-dash family is our custom extension to markdown to support various scenarios. 
    
:include-text-file: file-name.js

File will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* class path

# Inline Comments 

:include-cpp: simple.cpp {entry: "main", comments: "inline", bodyOnly: true}

# Callout Comments

If you already have comments inside your code it would be non effecient to repeat them inside documentation. 
Instead comments can be automatically extracted and presented as part of the text

:include-text-file: file-name-with-comments.js {lang: "javascript", commentsType: "inline"}

