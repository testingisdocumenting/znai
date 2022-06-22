# Markdown

An image can be included using standard Markdown syntax.

    ![alt text](regular-image.png)

![alt text](regular-image.png)

# Extension

`Znai` has `:include-image:` extension to provide additional features:
* title
* annotations
* fit 
* scale
* alignment
* presentation mode

# Title

Use `title` to add a title to an image.

    :include-image: castle.jpg {title: "Title of an image"}
    
:include-image: castle.jpg {title: "Title of an image"}

# Border

Use `border: true` to include a border around image.

    :include-image: image.png {border: true}
    
:include-image: regular-image.png {border: true}

# Fit

By default image occupies all available horizontal space:

:include-image: books.jpg {title: "wide image"}

Use `fit` parameter to fit an image to the text column width.

    :include-image: books.jpg {fit: true}

Note: You can click on the image to zoom into it

:include-image: books.jpg {fit: true, title: "auto scaled down image"}

# Scale

To scale image up or down use `scale` option, `1` is default, `0.5` is half an image size.   
    
    :include-image: books.jpg {scale: 0.3}

:include-image: books.jpg {scale: 0.3}

# Align

Use `align` option to align images left or right. 

    :include-image: books.jpg {align: "left", scale: 0.3}
    
:include-image: books.jpg {align: "left", scale: 0.3}

    :include-image: books.jpg {align: "right", scale: 0.3}
    
:include-image: books.jpg {align: "right", scale: 0.3}

# External Image

Pass external url in place of image to render image from a remote site 

    ![text](https://external-url)

    :include-image: https://external-url { align: "left" }

Note: Pass `:identifier: validate-external-links {validationPath: "org/testingisdocumenting/znai/cli/ZnaiCliConfig.java"}` parameter to validate image urls