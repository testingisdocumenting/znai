# Retina Displays

To have crisp documentation images on Retina displays use `SVG` images.

    :include-svg: with-groups.svg
    
:include-svg: with-groups.svg
        
# Ids To Reveal

If you have `groups` that you want to display while hiding everything else, pass the IDs to the `idsToReveal` property. 

    :include-svg: with-groups.svg {idsToReveal: ["partA", "partB"]}
    
:include-svg: with-groups.svg {idsToReveal: ["partA", "partB"]}

In presentation mode, groups will be displayed one at a time in the order specified.

This SVG image is taken and modified from [https://www.shareicon.net/pyramid-piramid-draw-stock-877888](https://www.shareicon.net/pyramid-piramid-draw-stock-877888)

:include-file: with-groups.svg {lang: "html"}
