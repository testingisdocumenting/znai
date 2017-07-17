# Markdown

Image can be include using standard markdown syntax

    ![alt text](regular-image.png)

![alt text](regular-image.png)

# Extension

MDoc adds additional extension to provide additional information:
* caption
* annotations
* fit parameters

# Caption

To add caption at the top of an image use

    :include-image: castle.jpg {caption: "Title of an image"}
    
:include-image: castle.jpg {caption: "Title of an image"}
    
To add caption at the bottom of an image use

    :include-image: castle.jpg {caption: "Title of an image", captionBottom: true}
    
:include-image: castle.jpg {caption: "Title of an image", captionBottom: true}

# Fit

Wide image occupies all available space.

:include-image: books.jpg 

To fit the image to the width of a text use `fit` parameter.

    :include-image: books.jpg {fit: true}

:include-image: books.jpg {fit: true}
