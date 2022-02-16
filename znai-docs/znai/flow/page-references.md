# Follow the Order

The best documentation should be arranged with a natural order to follow. 
Links to navigate to the next page are at the end of each page.

Create links to remind users of *essential* concepts introduced previously. There is a good chance that a reader skipped over these or forgot about them. 

Avoid: links that navigate users forward. It may break the flow of a documentation. 

# Links

To create an `external` link use:

```markdown
[Link Title](http://external/reference)
```
    
To refer `internal` page within your documentation use:

```markdown
[internal link](dir-name/file-name#optional-page-section-id)
```

Note: you can get `page-section-id` by hovering over a section title and pressing link icon. Your browser URL display the updated link.

## Links to Subsection

Linking to subsections is the same as linking to a top level section. [Here is an example](flow/page-references#links-links-to-subsection)

```markdown
[Here is an example](flow/page-references#links-links-to-subsection)
```

Use [Subsection Shortcut](#links) if a subsection is within the same page: 

```markdown
[Subsection Shortcut](#links)
```
  
## Index Page

Clicking this [index page](/) link will have the same effect as clicking the documentation title at the top of the **Navigation Panel**

To refer back to the top-level `index` page use:   

```markdown
[index page](/)
[index page](/#link-to-subsection)
```

# Downloads

Linking to a local file will deploy the file along with the generated documentation. 
Clicking the link will open a file using the browser's default method.

    Download [test json file](data/test.json)
    
Download [test json file](data/test.json)

# Validation

Local links are automatically validated during documentation build time. 
You will get a build time error if you refer to a page or a section/sub-section that does not exist.

Pass `--validate-external-links` to enable external links validation. 