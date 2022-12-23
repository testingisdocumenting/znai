---
identifier: {validationPath: ["python/my_func_usage.py", "python/utils.py"]}
---

# Backtick

To display inline code within a text, put it inside a backtick. 

    Example: To check if an Exchange is closed, you need to use `ExchangeCalendar`

Example: To check if an Exchange is closed, you need to use `ExchangeCalendar`

# From File

To display inline code from a file, use the `file` inlined code plugin. 

    Example: To access this feature, navigate to `:file: urlsample.txt`. 

Example: To access this feature, navigate to `:file: urlsample.txt`. 

# Validated Identifier

:include-java-doc: org/testingisdocumenting/znai/extensions/inlinedcode/IdentifierInlinedCodePlugin.java

:include-file: python/my_func_usage.py {title: "my_func_usage.py"}

Specify multiple files via [page local](plugins/default-parameters#page-local-defaults) or [global](plugins/default-parameters#global-defaults) plugin defaults 
to avoid repeating `validationPath` throughout the page.

:include-file: snippets/inlined-code-snippets.md {
  title: "page local default",
  startLine: "---",
  endLine: "---"
}

```markdown {title: "use page default validaiton path"}
my text `:identifier: my_func`
```
