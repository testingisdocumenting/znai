# Main Concepts

`Znai` has three levels of documentation organization:
* Chapters
* Pages
* Page Sections

`Znai` encourages authors to split their content across multiple pages. 

If you feel like you need to use nested headings, consider moving your content hierarchy one level up:
* Split overlong page into multiple ones
* Introduce chapters
* Focus on one thing at a time

It may help to show parallel with `OOP` concepts 
* Chapters as `packages`
* Pages as `classes`
* Page Sections as `methods`

It is a bad practice to have a class with loosely related methods. 
Similarly, it is a bad practice to have a long page with loosely related sections.

[Read more](https://testingisdocumenting.org/znai/flow/structure)

# Table of Contents

Each documentation must have `toc` file in its root. 
This file contains chapters and pages.

This is a `toc` file for this auto generated documentation.

:include-file: toc

Take a look at the left side bar and compare it with the file content.

The top entry, `Chapter One`, corresponds to the directory `chapter-one`. 
The nested entries like `Page Two`, corresponds to the file `page-two.md`.

# Embedding Content

To reduce documentation maintenance burden avoid copy and paste of code snippets.
Embed content by referencing existing files using `:include-file:` plugin instead.  

    :include-file: file-name.js {title: "Optional Title"}
    
:include-file: file-name.js {title: "Optional Title"}

File will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* all lookup paths listed in a `lookup-paths` file

[Read more](https://testingisdocumenting.org/znai/snippets/external-code-snippets)

# Meta

Each documentation must have the `meta.json` file in its root.
This `JSON` file contains documentation display name, type, and optional `View On` information.

:include-file: meta.json
