# Retina Displays

To have crisp documentation images on high DPI displays use `SVG` images.

    :include-svg: with-groups.svg
    
:include-svg: with-groups.svg
        
# Ids To Reveal

If you have `groups` that you want to display while hiding everything else, pass the IDs to the `idsToReveal` property. 

    :include-svg: with-groups.svg {idsToReveal: ["partA", "partB"]}
    
:include-svg: with-groups.svg {idsToReveal: ["partA", "partB"]}

In presentation mode, groups will be displayed one at a time in the order specified.

To force all specified parts to appear at once add this before (either in the same section, or at the start of a document).

    :include-meta: {allAtOnce: true}

This SVG image is taken and modified from [https://www.shareicon.net/pyramid-piramid-draw-stock-877888](https://www.shareicon.net/pyramid-piramid-draw-stock-877888)

:include-file: with-groups.svg {lang: "html"}

# Actual Size

SVGs may have additional white space around the content and have width and height property specified inside.
Use `actualSize: true` if you want to use the actual content only and effectively crop the white space. 
`width` and `height` will be overridden with the cropped size.

    :include-svg: with-groups.svg {idsToReveal: ["partC"], actualSize: true}
    
:include-svg: with-groups.svg {idsToReveal: ["partC"], actualSize: true}

Without `actualSize` there would be a gap created by hiding other parts.
 
# Scale

Use `scale: scaleFactor` to change the size of the SVG provided

    :include-svg: with-groups.svg {idsToReveal: ["partC"], actualSize: true, scale: 0.2}

:include-svg: with-groups.svg {idsToReveal: ["partC"], actualSize: true, scale: 0.2}
