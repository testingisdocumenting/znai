# Simple Snippet

It is very easy to add a code snippet or an output result.
All you have to do is indent your code with 4 spaces inside your Markdown document and
your code will be rendered like this.

    interface PriceService {
        Money calcPrice(String cuips, Integer quantity);
    }

:include-file: code-snippets.md {startLine: '# Simple Snippet', numberOfLines: 9, lang: 'markdown'}

Note: this method doesn't highlight code.

# Specifying Language
 
You can also specify a language to enable syntax highlighting for your snippet. 

    ```javascript
    import React, {Component} from 'react'

    class MyComponent extends Component {
        render() {
            /// ...
        }
    }
    ```

```javascript
import React, {Component} from 'react'

class MyComponent extends Component {
    render() {
        /// ...
    }
}
```

The following languages are supported:
* Java
* JavaScript
* Groovy
* C++
* Python
* Bash

`Znai` uses [prismjs](http://prismjs.com) library to provide syntax highlighting. 
Note that it is not being executed inside the browser, but rather applied during HTML generation.
  
# Title

Use the `title` property to specify a title.

    ```javascript {title: "ReactJS Component"}
    ...
    ```

```javascript {title: "ReactJS Component"}
import React, {Component} from 'react'

class MyComponent extends Component {
    render() {
        /// ...
    }
}
```

# Wide Code

Use the `wide` option to stretch wide code to occupy as much horizontal real estate as possible.  

    ```java {wide: true}
    class InternationalPriceService implements PriceService {
        private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {
            ...
        }
    }
    ```
    
```java {wide: true}
class InternationalPriceService implements PriceService {
    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {
        ...
    }
}
``` 

Without the `wide` option code will be aligned with the rest of the text and users can use scrollbars.   

```java
class InternationalPriceService implements PriceService {
    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {
        ...
    }
}
```

Note: Good placement of a *Wide Code* element is at the end of a page or a section to show the full version of a code sample.

# Wrap Code

Use the `wrap` option to stretch wide code to occupy as much horizontal real estate as possible.

    ```java {wrap: true}
    class InternationalPriceService implements PriceService {
        private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {
            ...
        }
    }
    ```

```java {wrap: true}
class InternationalPriceService implements PriceService {
    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {
        ...
    }
}
``` 

# Read More

If you have a large code snippet and you want to initially display only a small fraction use the `readMore` option with an **optional**
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

# Highlights

Use the `highlight` option to bring readers attention to important lines.


    ```java {highlight: "workingDir"}
    public class DocScaffolding {
        private final Path workingDir;
        private Map<String, List<String>> fileNameByDirName;
    
        public DocScaffolding(Path workingDir) {
        ...
        }
    ```
    
```java {highlight: "workingDir"}
public class DocScaffolding {
    private final Path workingDir;
    private Map<String, List<String>> fileNameByDirName;

    public DocScaffolding(Path workingDir) {
    ...
    }
```

Note: You will get a build-time error if the specified substring is not found in any of the snippet lines

It is recommended to pass a substring, but you can pass a line idx (starts from 0). 
Additionally you can combine two approaches and pass a list of things to highlight. 
    
    ```java {highlight: [2,  "..."]}
    public class DocScaffolding {
        private final Path workingDir;
        private Map<String, List<String>> fileNameByDirName;
    
        public DocScaffolding(Path workingDir) {
        ...
        }
    ```    

```java {highlight: [2,  "..."]}
public class DocScaffolding {
    private final Path workingDir;
    private Map<String, List<String>> fileNameByDirName;

    public DocScaffolding(Path workingDir) {
    ...
    }
```    

Note: Order of lines to highlight is reflected during presentation mode 
