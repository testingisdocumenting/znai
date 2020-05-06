---
type: two-sides
---

# Code First

[The include-jupyter extension](snippets/jupyter-notebook) automatically assigns `{meta: {rightSide: true}}` to output cells.
If page `type` is set to `two-sides` you will get automatic split between code and output.

Note: Content below is included using `include-jupyter`

:include-jupyter: snippets/jupyter/simple-notebook.ipynb

# Story First

Use `storyFirst` if you use a notebook with Markdown cells to tell a story. In this mode, code will be moved to the right
side and a reader can focus on the story itself.  

    :include-jupyter: notebook.ipynb {storyFirst: true}

:include-jupyter: src/test/resources/notebook-with-markdown-story.ipynb {storyFirst: true}
