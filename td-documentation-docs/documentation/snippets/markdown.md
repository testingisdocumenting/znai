# Single Markdown File

To reuse Markdown in several places without duplication, use the `include-markdown` plugin.

    :include-markdown: markdown-dir/md-to-include.md

:include-markdown: markdown-dir/md-to-include.md

# Multiple Markdown Files

You can also include all the Markdown files within a directory by using `include-markdowns`. 

This plugin can be used to generate a release notes or an FAQ page

    :include-markdowns: markdown-dir

:include-markdowns: markdown-dir

Note: `include-markdowns` displays the files of the specified directory based on the filename, in alphabetical order.

# Optional Markdown

When you document an open source project you may have different instructions based on where the documentation is deployed.
 
For example, this documentation has two versions, one deployed internally at Two Sigma and one is deployed externally using github pages. 
Most of the documentation parts are the same, but there are differences in sections like *Getting Started*.

We build documentation twice and the differences are handled by `:include-markdown:`'s `firstAvailable` parameter.

    :include-markdown: {firstAvailable: [
        "markdown-dir/getting-started-step-internal.md", 
        "markdown-dir/getting-started-step-external.md"]}
        
:include-markdown: {firstAvailable: [
    "markdown-dir/getting-started-step-internal.md", 
    "markdown-dir/getting-started-step-external.md"]}
 