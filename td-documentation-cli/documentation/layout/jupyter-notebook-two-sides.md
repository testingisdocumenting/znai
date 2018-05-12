---
type: two-sides
---

# Code First

[Include Jupyter](snippets/jupyter-notebook) automatically assigns `{meta: {rightSide: true}}` to output cells.
If page `type` is set to `two-sides` you will get automatic split between code and output.

Note: content below is included using `include-jupyter`

:include-jupyter: src/test/resources/notebook-with-markdown-story.ipynb

# Story First

Use `storyFirst` if you use a notebook with markdown cells to tell a story. In this mode code will be moved to the right
side and reader can focus on the story itself.  

    :include-jupyter: notebook.ipynb {storyFirst: true}

# Story First Example 

:include-jupyter: src/test/resources/notebook-with-markdown-story.ipynb {storyFirst: true}
