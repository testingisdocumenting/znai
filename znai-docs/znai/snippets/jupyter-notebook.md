# Include Notebook Section

Use `include-jupyter` to include a jupyter notebook or one of its parts.
Here is an example of a notebook:

:include-image: jupyter/notebook.png {fit: true, border: true}

To include one of its sections use,

```
:include-jupyter: jupyter/notebook.ipynb {
    includeSection: ["Loading Data From CSV"]
}
```

Markdown cells will be seamlessly integrated with the current page. 
Top level headers become this page top level header.

Note: without `includeSection`, the entire notebook content will be added.

:include-jupyter: jupyter/notebook.ipynb {
    includeSection: ["Loading Data From CSV"]
}

Note: Remember that you can define lookup paths for files like notebooks inside [lookup-paths](flow/lookup-paths) file, so you don't have
to copy and paste notebooks to your documentation directory.

# Exclude Section Title

Use `excludeSectionTitle` to exclude section title. It can be useful when you 
want to use section titles as example separators to be included into your guides.

```
:include-jupyter: jupyter/notebook.ipynb {
    includeSection: ["Average Prcing By Genre"],
    excludeSectionTitle: true
}
```

:include-jupyter: jupyter/notebook.ipynb {
    includeSection: ["Average Pricing By Genre"],
    excludeSectionTitle: true
}

# Two Sides

You will learn about the [Two Sides Layout](layout/two-sides-pages) in the Layout section. 
If you are are curious now for examples, jump to [Jupyter Two Sides example](layout/jupyter-notebook-two-sides)
