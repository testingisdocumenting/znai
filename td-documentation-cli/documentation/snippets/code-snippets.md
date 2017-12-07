# Simple Snippet

It is very easy to add a code snippet or an output result.
All you have to do is indent your code with 4 spaces inside your markdown document and
your code will be rendered like this.

    interface PriceService {
        Money calcPrice(String cuips, Integer quantity);
    }
    
This method doesn't highlight code by default. It is possible to provide a default language to use for highlighting using
[meta.json](meta file).

:include-file: code-snippets.md {startLine: '# Simple Snippet', numberOfLines: 9, lang: 'markdown'}

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

```bash
ls -l
export PATH=/home/path
echo $PATH
```

Snippet below is used to highlight the code as a javascript language

:include-file: code-snippets.md {startLine: '# Specifying Language', numberOfLines: 20, lang: 'markdown'}

Following languages are supported
* Java
* JavaScript
* Groovy
* C++
* Python
* Bash

`MDoc` uses [prismjs](http://prismjs.com) library to provide syntax highlighting. 
It is not being executed inside the browser: syntax highlighting is done during HTML generation
  
# Title

```javascript {title: "ReactJS Component"}
import React, {Component} from 'react'

class MyComponent extends Component {
    render() {
        /// ...
    }
}
```

Use `title` property to specify a title.

    ```javascript {title: "ReactJS Component"}
    ...
    ```


# Wide Code

To make regular text more readable we maintain a narrow column of text. You code snippets however can't always follow the 
same rules. In case of a wide code snippets we expand the horizontal boundaries and center the source code to fit.

:include-file: wide-code.java {title: "Wide Code"}

# Read More

If you have a large code snippet and you want to initially display only a small fraction use `readMore` option with an **optional**
`readMoreVisibleLines` option to specify a number of initial lines displayed (default is 8).

    ```java {readMore: true, readMoreVisibleLines: 3}
    public class DocScaffolding {
        private final Path workingDir;
        private Map<String, List<String>> fileNameByDirName;
        ...
    ```

```java {readMore: true, readMoreVisibleLines: 3}
public class DocScaffolding {
    private final Path workingDir;
    private Map<String, List<String>> fileNameByDirName;

    public DocScaffolding(Path workingDir) {
        this.workingDir = workingDir;
        this.fileNameByDirName = new LinkedHashMap<>();
    }

    public void create() {
        createPages();
        createToc();
        createMeta();
        createIndex();
        createLookupPaths();
    }

    private void createLookupPaths() {
        createFileFromResource("lookup-paths");
    }

    private void createMeta() {
        createFileFromResource("meta.json");
    }
}
```

# Inlined

To inline code within a text dimply put it inside a back tick. 
E.g. To check if an Exchange is closed, you need to use `ExchangeCalendar`

:include-file: code-snippets.md {startLine: '# Inlined', numberOfLines: 4, lang: 'markdown'}
