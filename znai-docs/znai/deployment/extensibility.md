# Extensibility

Znai recognizes automatically jar files put on the classpath

A znai extension is a jar file containing

one or several of the resources under `META-INF/services` :

- `org.testingisdocumenting.znai.extensions.fence.FencePlugin`
- `org.testingisdocumenting.znai.extensions.include.IncludePlugin`
- `org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin`

The resources listed above are flat files containing lists of fully qualified classnames of plugins.

one resource `META-INF/znai/javascript-files.txt`

- `org.testingisdocumenting.znai.extensions.ExtraJavaScript`

The file  `org.testingisdocumenting.znai.extensions.ExtraJavaScript` contains a flat list of fully qualified names of JavaScript resources to load.

example
```
META-INF/znai/js/extension1/somename.js
META-INF/zna/js/extension2/somename2.js
```

When running the maven goals `znai:preview` or `znai:build` or the equivalent with the CLI, 
the generated index page would load the javascript files. 
The java implementation of the extensions will be loaded by znai the same way that znai loads its own plugins.