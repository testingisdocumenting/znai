# Markdown

An image can be included using standard Markdown syntax.

    ![alt text](regular-image.png)

![alt text](regular-image.png)

# Extension

`Znai` adds additional extension to provide additional information:
* caption
* annotations
* fit parameters

# Caption

To add a caption at the top of an image use:

    :include-image: castle.jpg {caption: "Title of an image"}
    
:include-image: castle.jpg {caption: "Title of an image"}
    
To add a caption at the bottom of an image use:

    :include-image: castle.jpg {caption: "Title of an image", captionBottom: true}
    
:include-image: castle.jpg {caption: "Title of an image", captionBottom: true}

# Border

Use `border: true` to include a border around image.

    :include-image: image.png {border: true}
    
:include-image: regular-image.png {border: true}

# Fit

An unfitted image will occupy all available space:

:include-image: books.jpg 

To fit the image to the width of a text use the `fit` parameter.

    :include-image: books.jpg {fit: true}

:include-image: books.jpg {fit: true}

# Scale Ratio

To scale image up or down use `scaleRatio` option, `1` is default, `0.5` is half image size.   
    
    :include-image: books.jpg {scaleRatio: 0.3}

:include-image: books.jpg {scaleRatio: 0.3}

# Align

Use `align` option to align images left or right. 

    :include-image: books.jpg {align: "left", scaleRatio: 0.3}
    
:include-image: books.jpg {align: "left", scaleRatio: 0.3}

    :include-image: books.jpg {align: "right", scaleRatio: 0.3}
    
:include-image: books.jpg {align: "right", scaleRatio: 0.3}

