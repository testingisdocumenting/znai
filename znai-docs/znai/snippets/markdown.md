# Single Markdown File

To reuse Markdown in several places without duplication, use the `include-markdown` plugin.

:include-file: markdown-dir/md-to-include.md {autoTitle: true}

    :include-markdown: markdown-dir/md-to-include.md

:include-markdown: markdown-dir/md-to-include.md

# Optional Markdown

When you document an open source project you may have different instructions based on where the documentation is deployed.
 
For example, this documentation has two versions, one deployed internally at Two Sigma and one deployed externally using GitHub Pages.
Most of the documentation parts are the same, but there are differences in sections like *Getting Started*.

We build documentation twice and the differences are handled by `:include-markdown:`'s `firstAvailable` parameter.

    :include-markdown: {firstAvailable: [
        "markdown-dir/getting-started-step-internal.md", 
        "markdown-dir/getting-started-step-external.md"]}
        
:include-markdown: {firstAvailable: [
    "markdown-dir/getting-started-step-internal.md", 
    "markdown-dir/getting-started-step-external.md"]}
 
# Partial Markdown

Use `surroundedBy` to include portion of a markdown from existing markdown file, e.g. `readme.md` 

Given an existing markdown files with markers

:include-file: markdown-dir/markdown-with-markers.md {title: "readme.md"}

```markdown {title: "partial include"}
:include-markdown: markdown-dir/markdown-with-markers.md {surroundedBy: "<> (marker-one)"}
```

:include-markdown: markdown-dir/markdown-with-markers.md {surroundedBy: "<> (marker-one)"}

# Multiple Markdown Files

You can also include all the Markdown files within a directory by using `include-markdowns`.

This plugin can be used to generate release notes or an FAQ page.

:include-file: faq-collection/2022-07-14-install.md { autoTitle: true } 

:include-file: faq-collection/2022-07-15-preview.md { autoTitle: true } 

    :include-markdowns: faq-collection {sort: "ascending"}

:include-markdowns: faq-collection {sort: "ascending"}

Note: `include-markdowns` renders files of the specified directory based on the filename (by default in descending alphabetical order).

