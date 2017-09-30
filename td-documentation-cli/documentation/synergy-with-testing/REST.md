# Request and Response Capture

It helps to understand REST API if there are clear scenarios defined.
Showing request used and response received makes your documentation less abstract.

Instead of manually copy&paste the responses back to your documentation consider running tests instead and automatically
capture relevant artifacts.

# WebTau

[WebTau](http://mdoc.app.twosigma.com/webtau/REST/getting-started) is the framework I use to write,
run and capture REST tests artifacts.

The bare minimum test in WebTau looks like this

:include-file: examples/rest/restGet.groovy

# Capture REST Artifacts

To capture artifacts use `http.doc.capture`

:include-file: examples/rest/restPost.groovy {commentsType: "inline"}

Captured artifact is a json file that looks like this

:include-file: REST/employee-get.json

# Document REST calls

Once artifact is captured, include it for documentation with `rest-test` plugin

    :include-rest-test: REST/employee-get.json

Result looks like

:include-rest-test: REST/employee-get.json

or

:include-rest-test: REST/employee-post.json

Note: all the asserted values will be automatically highlighted for your users to help you bring their attention to
values of interest.