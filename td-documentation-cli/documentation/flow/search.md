# Local

To perform a local search press `/` key or click magnifying glass in the top right corner.

In [documentation structure](flow/structure) we learnt that documentation consist of  
* Chapters
* Pages
* Page Sections

Each of these names is very crucial to how your search performs. Search treats each *Page Section* as an independent unit.
Each *Page Section* has an internally defined title that is a combination of all three titles.

Title match during search yields the highest score. 

For example this current section full title is: *Flow Search Local*.

As your documentation grows, keep checking how easy it is to navigate to a section of your documentation using `/`.

Avoid: avoid using generic names in your page titles and page section titles. You should not have dozens of pages called *Introduction*  

# Global 

Global search is performed by [search/](https://search.app.twosigma.com).

Global search consider each page section as an independent entry as well. 

In case of global search full title gets expanded to include documentation title, so the full title of the
current section will be: *MDoc Flow Search Global*.

Go now and [search](https://search.app.twosigma.com) for *mdoc search* and see what results are there.   

In case of global search it is even more important to come up with the good names for 
* Chapters
* Pages
* Page Sections

To make your product more discoverable consider naming your *page sections* and *page titles* to match potential user queries.
E.g. consider page called *Getting Started*

```columns 
left:
**DO**

    # Node installation 
    
    To install node.js use ...
    
right:
**DON'T**

    # Prerequsites
    
    To install node.js use ...    
```
