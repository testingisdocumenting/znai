# Markdown

MDoc uses Markdown, a markup language that uses ASCII to represent styles and page structure.

```markdown-and-result
# Header

Normal paragraph text. Some *italic* and **bold**.
Followed by bullet points:
* Apple
* Banana
* Water
```

# External References

Example of how to keep your documentation up to date by referencing existing resource.

```markdown
# Server Configuration

:include-file: config/server.config
```

Note:  Ths `include-file` macro is a `mdoc` extension and not part of a standard Markdown syntax.
