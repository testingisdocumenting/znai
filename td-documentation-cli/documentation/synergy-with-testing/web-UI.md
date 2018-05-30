# Auto Annotations

How to annotate images using the `include-image` plugin [was covered previously](visuals/image-annotations) .
Now let's automate the screenshot and annotations assignment process.

:include-java: com/twosigma/testing/webtau/WebTauDslDemo.java {bodyOnly: true, entry: "main"}

Example above is using a Selenium-based framework to automatically take a screenshot and retrieve annotations positions. 
Once we have the information we can include the annotated image in our documentation.

    :include-image: test.png {annotationsPath: 'test.json'}

The result is below:

:include-image: test.png {annotationsPath: 'test.json'}

