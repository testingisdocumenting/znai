# Auto Highlight

It is very easy to add a code snippet with an automatic code highlighting.
All you have to do is indent your code with 4 spaces inside your markdown document and 
you code will be auto highlighted.

    interface PriceService {
        Money calcPrice(String cuips, Integer quantity);
    }
    
This method attempts to guess the language and highlight accordingly. Markdown used to create code snippet above follows

:include-text-file: code-snippets.md {startLine: 'interface PriceService', numberOfLines: 3, lang: 'markdown', title: 'markdown'}
    

# Specifying Language
 
You can also specify a language. That maybe useful if there is the snippet is not large enough for auto detection.

```javascript
import React, {Component} from 'react'

class MyComponent extends Component {
    render() {
        /// ...
    }
}
```

Snippet below is used to highlight the code as a javascript language

:include-text-file: code-snippets.md {startLine: '```javascript', numberOfLines: 9, lang: 'markdown', title: 'markdown'}

Following languages are supported
* Java
* JavaScript
* Groovy
* Python


We use highlightjs library to provide syntax highlighting 

# Wide Code

To make regular text more readable we maintain a narrow column of text. You code snippets however can't always follow the 
same rules. In case of a wide code snippets we expand the horizontal boundaries and center the source code to fit.

```java
class InternationalPriceService implements PriceService {
    private static void LongJavaInterfaceNameWithSuperFactory createMegaFactory(final ExchangeCalendar calendar) {
        ...
    }
}
```

# From File

To boost documentation maintainability you can refer text and code snippets from already existing files.

    :include-text-file: file-name.js
    
Include-dash family is our custom extension to markdown to support various scenarios. 
    
:include-text-file: file-name.js

File will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* class path
