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