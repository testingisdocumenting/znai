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

# CLI parameter

Use `--lookup-paths` CLI parameter to add additional paths to lookup files.  

```cli
znai --lookup-paths /extra/path-one /extra/path-two
```

# Zip and Jar Lookup

When Znai encounters zip or jar file listed inside `lookup-paths` it will unpack the archives into a temporary location
and will use those locations to resolve files

```txt {title: "lookup-paths"}
../sources.zip
../module/archive.jar
```

    :include-file: dir/inside-zip-b.txt

:include-file: dir/inside-zip-b.txt {autoTitle: true}

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

# Class Path Lookup

Znai is written using Java and can access resources from the classpath. 
Using class path lookup you can include snippets from projects deployed to, for example, Maven Central.

:include-file: maven-class-path.xml {highlight: "classifier"}

    :include-java: org/junit/Assert.java {entry: "fail(String)"}

:include-java: org/junit/Assert.java {entry: "fail(String)"}

