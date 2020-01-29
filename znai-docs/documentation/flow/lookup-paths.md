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

# HTTP Lookup Location

If files you want to include are not part of your project, you can add an HTTP base URL to `lookup-paths`.

```txt {title: "lookup-paths"}
../examples
../module/src/main/java
https://raw.githubusercontent.com/twosigma/webtau/master
```

If the file is not found using local locations, it will be fetched from the provided urls.

    :include-file: .travis.yml
 
:include-file: .travis.yml {lang: "yaml"}

