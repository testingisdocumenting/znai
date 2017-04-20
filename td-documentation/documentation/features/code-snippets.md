# Simple Snippet

It is very easy to add a code snippet or an output result.
All you have to do is indent your code with 4 spaces inside your markdown document and
your code will be rendered like this.

    interface PriceService {
        Money calcPrice(String cuips, Integer quantity);
    }
    
This method doesn't highlight code by default. It is possible to provide a default language to see for highlighting using
[meta.json](meta file).
Markdown used to create code snippet above follows

:include-file: code-snippets.md {startLine: '# Simple Snippet', numberOfLines: 9, lang: 'markdown', title: 'markdown'}
    

# Specifying Language
 
You can also specify a language. 
That maybe useful if there is the snippet is not large enough for auto detection.

```javascript
import React, {Component} from 'react'

class MyComponent extends Component {
    render() {
        /// ...
    }
}
```

Snippet below is used to highlight the code as a javascript language

:include-file: code-snippets.md {startLine: '# Specifying Language', numberOfLines: 14, lang: 'markdown', title: 'markdown'}

Following languages are supported
* Java
* JavaScript
* Groovy
* Python


We use highlightjs library to provide syntax highlighting 

# Wide Code

To make regular text more readable we maintain a narrow column of text. You code snippets however can't always follow the 
same rules. In case of a wide code snippets we expand the horizontal boundaries and center the source code to fit.

:include-file: wide-code.java

# Inlined

To inline code within a text dimply put it inside a back tick. 
E.g. To check if an Exchange is closed, you need to use `ExchangeCalendar`

:include-file: code-snippets.md {startLine: '# Inlined', numberOfLines: 4, lang: 'markdown', title: 'markdown'}
