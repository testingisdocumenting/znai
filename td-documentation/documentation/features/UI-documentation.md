# Complex Domain

Not every UI can be as simple as one input box.

:include-image: idea.png 

> Documenting and Presenting your UI
> should be a joy

# Annotated Images

:include-image: word-toolbar.jpg {annotationsPath: 'annotations1.json'}

there are different types of annotations:
* circles
* rectangles
* arrows
* etc

:include-image: screenshot1.png {annotationsPath: 'annotations2.json'}

Annotations are stored as a simple json file

:include-file: annotations2.json

And then easily integrated with documentation by using an `image` plugin

:include-file: UI-documentation.md {startLine: '# Annotated Images', endLine: '* etc', lang: 'markdown'}

# Editing Annotations

:include-image: editor-overview.png {annotationsPath: 'editor-overview.json'}

# Presentation Mode

Annotated images automatically participate in presentation mode. Annotations appear one by one and scale to match the
zoom level of a presentation.
  


