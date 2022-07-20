# Request and Response Capture

[Open API](snippets/open-API) helps to define a formal API specification.
And real examples of request/response helps to clarify. 

Instead of manually copy-and-pasting the responses to your documentation, consider running tests and automatically
capturing the relevant artifacts.

# WebTau

[WebTau](https://testingisdocumenting.org/webtau) (short for web test automation) is a testing API, command line tool and a framework to write unit, 
integration and end-to-end tests. Test across REST-API, Graph QL, Browser, Database, CLI and Business Logic with consistent set of matchers and concepts.

Here is an example of REST API test with test artifacts capture 

:include-file: webtauexamples/restPost.groovy {
  title: "happy path test scenario",
  surroundedBy: "// example",
  commentsType: "inline"
}

# Documentation Pipeline

Documentation pipeline can look like

:include-flow-chart: documentation-flow.json

`````markdown {title: "znai example of using test artifacts"}
# Create Employee

:include-open-api: openapi/api-spec.json {operationId: "createEmployee" }

```columns
left: 
:include-json: doc-artifacts/employee-post/request.json { title: "request payload" }

right: 
:include-json: doc-artifacts/employee-post/response.json { 
  title: "response payload", 
  pathsFile: "doc-artifacts/employee-post/paths.json" 
}
```
`````


# Create Employee

:include-open-api: examples/openapi/api-spec.json {operationId: "createEmployee" }

```columns
left: 
:include-json: doc-artifacts/employee-post/request.json { title: "request payload" }

right: 
:include-json: doc-artifacts/employee-post/response.json { 
  title: "response payload", 
  pathsFile: "doc-artifacts/employee-post/paths.json" 
}
```

