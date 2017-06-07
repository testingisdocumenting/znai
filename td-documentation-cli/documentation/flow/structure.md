# Building blocks

MDoc has three levels of documentation organization:
* Chapters
* Pages
* Page Sections

MDoc encourages to split content into multiple pages.
If you feel like you need to use nested headings, consider moving your content hierarchy one level up:
* split page into multiple ones
* introduce chapters
* focus on one thing at a time

# Similarity with OOP

It may help to show parallel with `OOP` concepts 
* Pages as `classes`
* Page Sections as `methods`
* Chapters as `packages`

It is a bad practice to have one Class with dozens of unrelated methods. 
Similarly it is a bad practice to have one page with dozens of unrelated sections.

# Table Of Contents

Each documentation must have `toc` file in its root. 
File contains chapters and pages.

This is a `toc` file for this documentation.

:include-file: toc

Take a look at the left side bar and compare it with the file content.

Top level entry `introduction` correspond to directory names. 
Nested entry `rationale` correspond to file name `rationale.md`.

# Sub Headings

Only a first level heading is treated as a first class citizen:
* Part of **TOC**
* Smallest unit of **Search result**
```   
# First Class Citizen
```

Nested sub headings only add visual distinction.
If you use them to create your document outline chances are you need to consider splitting your page. 

    ## Sub heading
    content of sub heading
    
    ### Sub Sub heading
    content of sub sub heading

## Sub heading
content of sub heading

### Sub Sub heading
content of sub sub heading


# Meta

Each documentation must have `meta.json` file in its root.
Json contains documentation display name and type.

:include-file: meta.json