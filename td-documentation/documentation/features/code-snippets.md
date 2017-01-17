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
