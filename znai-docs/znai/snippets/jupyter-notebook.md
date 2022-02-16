# Code and Output

Use `include-jupyter` to include [Jupyter](https://jupyter.org/) notebook inside your documentation. 

    :include-jupyter: jupyter/simple-notebook.ipynb
    
:include-jupyter: jupyter/simple-notebook.ipynb

Note: Remember that you can define lookup paths for files like notebooks inside [lookup-paths](flow/lookup-paths) file, so you don't have
to copy and paste notebooks to your documentation directory.

# Seamless Markdown Integration

Markdown from your notebook will be seamlessly integrated into your current page. First level `# headers` will
become part of Table Of Contents and part of a search unit. 

:include-image: jupyter/notebook.png {fit: true}

    :include-jupyter: notebook-with-markdown-story.ipynb

Note: below text is auto generated, including the **Panda** section 
:include-jupyter: src/test/resources/notebook-with-markdown-story.ipynb
     
# Two Sides

You will learn about the [Two Sides Layout](layout/two-sides-pages) in the Layout section. 
If you are are curious now for examples, jump to [Jupyter Two Sides example](layout/jupyter-notebook-two-sides)
