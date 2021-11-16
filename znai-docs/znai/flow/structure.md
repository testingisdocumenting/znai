# Building Blocks

`Znai` has three levels of documentation organization:
* Chapters
* Pages
* Page Sections

`Znai` encourages authors to split their content across multiple pages. 

If you feel like you need to use nested headings, consider moving your content hierarchy one level up:
* Split overlong page into multiple ones
* Introduce chapters
* Focus on one thing at a time

# Similarity with OOP

It may be useful to compare documentation design to an object-oriented programming approach:
* Chapters as `packages`
* Pages as `classes`
* Page Sections as `methods`

It is a bad practice to have a class with loosely related methods. 
Similarly, it is a bad practice to have a long page with loosely related sections.

# Table of Contents

Each documentation must have `toc` file in its root. 
This file contains chapters and pages.

This is a `toc` file for this documentation.

:include-file: toc

Take a look at the left side bar and compare it with the file content.

The top entry, `introduction`, corresponds to the directory of the same name. 
The nested entry, `rationale`, corresponds to the file `rationale.md`.

# Sub Headings

Only a first level heading is treated as a first class citizen:
* Part of **TOC**
* Smallest unit of **search result**
```   
# First Class Citizen
```

Nested sub headings only add visual distinction within a page.

    ## Sub heading
    content of sub heading
    
    ### Sub Sub heading
    content of sub sub heading

## Sub heading
content of sub heading

### Sub Sub heading
content of sub sub heading

# Meta

Each documentation must have the `meta.json` file in its root.
This `JSON` file contains documentation display name, type, and optional `View On` information.

:include-file: meta.json
