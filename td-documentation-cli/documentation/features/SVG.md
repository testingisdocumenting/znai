# Retina Displays

To have crisp documentation images on Retina displays use `SVG` images.

    :include-svg: with-groups.svg
    
:include-svg: with-groups.svg
        
# Ids To Reveal

If you have `groups` that you want to reveal and hide everything else, pass `idsToReveal` to display. 

    :include-svg: with-groups.svg {idsToReveal: ["partA", "partB"]}
    
:include-svg: with-groups.svg {idsToReveal: ["partA", "partB"]}

Additionally it affects Presentation mode: parts will be displayed one at a time in the specified order.

Image is taken and modified from [https://www.shareicon.net/pyramid-piramid-draw-stock-877888](https://www.shareicon.net/pyramid-piramid-draw-stock-877888)

:include-file: with-groups.svg {lang: "html"}
