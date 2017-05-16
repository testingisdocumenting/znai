# Auto Annotations

We saw (todo link) how you can annotate images using `include-image` plugin.
Now let's automate the screenshot and annotations assigning process.

:include-java: com/twosigma/testing/webtau/WebTauDslDemo.java {bodyOnly: true, entry: "main"}

Example above is using a Selenium based framework to automatically take screenshot and retrieve annotations positions. 
Once we have the information we can include the annotated image in our documentation.

    :include-image: test.png {annotationsPath: 'test.json'}

Result is below 

:include-image: test.png {annotationsPath: 'test.json'}

