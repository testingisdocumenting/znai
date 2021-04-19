# Annotations File

    :include-image: word-toolbar.jpg {annotationsPath: 'annotations.json'}

:include-image: word-toolbar.jpg {annotationsPath: 'annotations.json'}

:include-file: annotations.json {title: "annotations.json"}

There are different types of annotations:
* Circles
* Rectangles
* Arrows
* etc.

# Integration With Testing

It is possible to generate an annotations file by using UI testing framework.

For example [webtau](https://github.com/testingisdocumenting/webtau) automatically generates an annotations file in addition to 
capturing a screenshot. 

# Path Shortcut 

You don't need to specify annotations path, if annotation file has `json` extension and matches file name and path of the image

    :include-image: word-toolbar.jpg

:include-image: word-toolbar.jpg

:include-file: annotations.json {title: "word-toolbar.json"}

# Presentation Mode

Annotated images automatically participate in presentation mode. Annotations appear one by one and scale to match the
zoom level of a presentation.
  


