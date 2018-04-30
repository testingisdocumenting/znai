# Follow the Order

Most of the documentation should have a natural order to follow. 
Links to navigate to a next page are at the end of each page.

Crate links to remind an *essential* concept introduce previously. There is a chance that a person skipped over it or forgot. 

Avoid: links that navigate users forward. It breaks the flow of a documentation. 

# Links

To create an `external` link use 

```markdown
[Link Title](http://external/reference)
```
    
To refer `internal` page within your documentation use 

```markdown
[internal link](dir-name/file-name#optional-page-section-id)
```

Note: you can get `page-section-id` by hovering over a section title and pressing link icon. Your browser URL will have updated link.

To refer back `index` page use   

```markdown
[index link](/)
```

Clicking [Index page](/) have the same effect as clicking documentation title on the **Navigation Panel**

# Downloads

Linking to a local file will deploy the file along with the generated documentation. 
Clicking the link will open a file using browser's default method.

    Download [test json file](data/test.json)
    
Download [test json file](data/test.json)
