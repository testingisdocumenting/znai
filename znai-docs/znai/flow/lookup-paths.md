# Files Reference Lookup

When you refer to a file using plugins like

 `:include-file: my-file.cpp` 
 
 `Znai` tries to find the file in following locations:

* directory with the markup file that refers `my-file.cpp`
* documentation root directory
* locations enumerated inside `<doc-root>/lookup-paths` file

```txt {title: "lookup-paths"}
../examples
../module/src/main/java
```

# Class Path Lookup

Znai is written using Java and can access resources from the classpath. 
Using class path lookup you can include snippets from projects deployed to, for example, Maven Central.

:include-file: maven-class-path.xml {highlight: "classifier"}

    :include-java: org/junit/Assert.java {entry: "assertArrayEquals"}

--:include-java: org/junit/Assert.java {entry: "assertArrayEquals"}

# HTTP Lookup Location

If files you want to include are not part of your project, you can add an HTTP base URL to `lookup-paths`.

```txt {title: "lookup-paths"}
../examples
../module/src/main/java
https://raw.githubusercontent.com/testingisdocumenting/webtau/master
```

If the file is not found using local locations, it will be fetched from the provided urls.

    :include-file: .travis.yml
 
:include-file: .travis.yml {lang: "yaml"}


