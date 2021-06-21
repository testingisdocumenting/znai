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

For example [webtau](https://github.com/testingisdocumenting/webtau) automatically generates an annotations file in addition to 
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
  


