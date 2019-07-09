# Highlight Parts

Use the `include-xml` plugin to bring attention to a certain place in a `XML` file. 

Comma-separated paths specified inside `paths` will be highlighted.

    :include-xml: menu.html {paths: ["ul.@class", "ul.li[1].@class", "ul.li[2]"]}
    
:include-xml: menu.html {paths: ["ul.@class", "ul.li[1].@class", "ul.li[2]"]}

Note: Children index in `paths` starts with 0 and is associated with a tag

    :include-xml: simple.xml {paths: ["root.a[1]", "root.b[0]", "root.c[0]"]}
    
:include-xml: simple.xml {paths: ["root.a[1]", "root.b[0]", "root.c[0]"]}

# Use Cases

`include-xml` and `paths` can be used to document:

* XML Config
* CSS selectors
* ReactJS properties
* AngularJS templates

In presentation mode, paths will be highlighted one at a time.    

# Title

Use the `title` property to specify a title.

    :include-xml: menu.html {title: "Menu snippet"}
    
:include-xml: menu.html {title: "Menu snippet"}
