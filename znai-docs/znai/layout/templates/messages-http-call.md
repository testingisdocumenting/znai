:include-groovy: org/testingisdocumenting/testing/examples/restapi/WebTauRestAPIGroovyTest.groovy {
  title: "${method?upper_case} messages",
  entry: "${method}Messages",
  bodyOnly: true,
  excludeRegexp: "http.doc"
}

```columns
left:
:include-json: chat-${method}-messages/request.json {
  title: "${method?upper_case} /messages request"
}
  
right:
:include-json: chat-${method}-messages/response.json {
  title: "${method?upper_case} /messages response",
  pathsFile: "chat-post-messages/paths.json"
}
```
