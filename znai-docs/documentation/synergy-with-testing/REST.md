# Request and Response Capture

It helps in understanding a REST API if there are clear scenarios defined.
Showing examples of requests used and responses received makes your documentation less abstract.

Instead of manually copy-and-pasting the responses back to your documentation, consider running tests and automatically
capturing the relevant artifacts.

# WebTau

[WebTau](http://mdoc.app.twosigma.com/webtau/REST/getting-started) is the framework to write,
run, and capture REST tests artifacts.

The bare minimum test in WebTau looks like this:

:include-file: examples/rest/restGet.groovy

# Capture REST Artifacts

To capture artifacts use `http.doc.capture`

:include-file: examples/rest/restPost.groovy {commentsType: "inline"}

Captured artifact is a `JSON` file that looks like this:

:include-file: REST/employee-get.json

# Document REST calls

Once the artifact is captured, include it for documentation with the `rest-test` plugin.

    :include-rest-test: REST/employee-get.json

The result looks like:

:include-rest-test: REST/employee-get.json

or:

:include-rest-test: REST/employee-post.json

Note: all the asserted values will be automatically highlighted for your users to help you bring their attention to
values of interest.
