# Badges

Use `image` fence plugin to display Image and manually provide badge coordinates 

    ```image testingisdocumenting.png {scale: 0.5} 
    840,600
    1680,1400
    ```

```image testingisdocumenting.png {scale: 0.5} 
840,600
1680,1400
```

Note: Color of badges change based on the background color

# Manual Coordinates

Hover mouse over image during [Preview Mode](introduction/getting-started#preview-mode) to display coordinates under the cursor. Use the displayed coordinates to update the position.  

# Badge Textual Description

Put ordered list right before or after an annotated image to associate text with the badges.

````markdown {title: "ordered list markdown example to provide contextual information"}
1. Use automated testing to exercise happy paths and capture test artifacts
2. Use captured test artifacts to supercharge your documentation

```image testingisdocumenting.png {scale: 0.5} 
840,600
1680,1400
```
````

Note: Hover over image annotations to display automatic tooltip. 
\
Hover over an item text to highlight the annotation on the image.


1. Use automated testing to exercise happy paths and capture test artifacts
2. Use captured test artifacts to supercharge your documentation

```image testingisdocumenting.png {scale: 0.5} 
840,600
1680,1400
```

# Pixel Ratio

Use `pixelRatio` to display HiDPI images and use logical coordinates for the annotations

    ```image testingisdocumenting.png {pixelRatio: 2} 
    420,300
    840,700
    ```

```image testingisdocumenting.png {pixelRatio: 2} 
420,300
840,700
```

# Rectangles And Arrows

Use `rect` and `arrow` as first column value to render arrow or rectangle annotation

    ```image testingisdocumenting.png {pixelRatio: 2} 
    rect,60,110,420,430
    arrow,485,810,310,474
    ```

Add a text block after coordinates to provide tooltip data. Markdown is supported.

    ```image testingisdocumenting.png {pixelRatio: 2} 
    rect,60,110,420,430,Note: zone description
    arrow,485,810,310,474,destination **description**
    ```

```image testingisdocumenting.png {pixelRatio: 2} 
rect,60,110,420,430,Note: zone description
arrow,485,810,310,474,destination **description**
```

Note: Hover over image annotations to display automatic tooltip.

# Annotations File

    :include-image: word-toolbar.jpg {annotationsPath: 'annotations.json'}

:include-image: word-toolbar.jpg {annotationsPath: 'annotations.json'}

:include-file: annotations.json {title: "annotations.json"}

There are different types of annotations:
* Circles
* Badges
* Rectangles
* Arrows
* etc.

# Integration With Testing

It is possible to generate an annotations file by using UI testing framework.

For example [WebTau](https://github.com/testingisdocumenting/webtau) automatically generates an annotations file in addition to 
capturing a screenshot. 

# Path Shortcut 

You don't need to specify annotations path, if annotations file matches file name and path of the image and 
has `json` extension.

Add `annotate:true` to automatically use matching annotations file

    :include-image: word-toolbar.jpg {annotate: true}

:include-image: word-toolbar.jpg {annotate: true}

:include-file: annotations.json {title: "word-toolbar.json"}

# Presentation Mode

Annotated images automatically participate in presentation mode. Annotations appear one by one and scale to match the
zoom level of a presentation.
  


