# Local

To perform a local search of your documentation press `/` key or click the search section in the side panel on the left.

In [documentation structure](flow/structure) we learned that documentation consist of  
* Chapters
* Pages
* Page Sections

These entities play different and important roles in search. Search treats each *Page Section* as an independent unit.
Each *Page Section* has an internally defined title that is a combination of all three titles.

Title match during search yields the highest score. 

For example this current section full title is: *Flow Search Local*.

As your documentation grows, keep checking how easy it is to navigate to a section of your documentation using `/`.

Avoid: using generic names in your page titles and page section titles. You should not have dozens of pages called *Introduction*  

# Global 

:include-markdown: {firstAvailable: ["search-global-override.md", "search-global.md"]}