# Scenarios

You provide `REST end points` so users can execute various scenarios.
You need to test those scenarios and then document them.

To automate the process let's capture executed scenarios and use them inside your documentation.

# Test Artifacts

To capture artifacts use `http.doc.capture`

:include-file: examples/rest/restPost.groovy {commentsType: "inline"}

Captured artifact is a json file that looks like this

:include-file: examples/employee-get.json

# Document REST calls

If you use [mdoc](http://mdoc.app.twosigma.com/mdoc) as your documentation system use `include-rest-test` directive
to include test artifacts.

    :include-rest-test: examples/employee-get.json

Result looks like

:include-rest-test: examples/employee-get.json

or

:include-rest-test: examples/employee-post.json

Note: all the asserted values will be automatically highlighted for your users to help you bring their attention to
values of interest.