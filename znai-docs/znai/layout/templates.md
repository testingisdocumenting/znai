# Re-usable patterns

Let's consider a chat REST API example where we want to display a sample input, example code snippet, and sample output.
Here how documentation may look like.

Use `:file: chat-post-messages/request.method.txt` `:file: chat-post-messages/request.url.txt` to send a new message

:include-groovy: org/testingisdocumenting/testing/examples/restapi/WebTauRestAPIGroovyTest.groovy {
  title: "POST messages",
  entry: "postMessages",
  bodyOnly: true,
  excludeRegexp: "http.doc"
}

```columns
left:
:include-json: chat-post-messages/request.json {
  title: "POST /messages request"
}
  
right:
:include-json: chat-post-messages/response.json {
  title: "POST /messages response",
  pathsFile: "chat-post-messages/paths.json"
}
```

This is the source to render the layout above

    :include-groovy: org/testingisdocumenting/testing/examples/restapi/WebTauRestAPIGroovyTest.groovy {
      title: "POST messages",
      entry: "postMessages",
      bodyOnly: true,
      excludeRegexp: "http.doc"
    }
    
    ```columns
    left:
    :include-json: chat-post-messages/request.json {
      title: "POST /messages request"
    }
      
    right:
    :include-json: chat-post-messages/response.json {
      title: "POST /messages response",
      pathsFile: "chat-post-messages/paths.json"
    }
    ```

Note: request and response were automatically captured using [WebTau](synergy-with-testing/REST-API) testing framework.
    
We need to document other methods as well, and as you can imagine, the repetition will take place.

# Extract A Pattern

Let's move a block we plan to repeat into a separate file and replace hardcoding of entries like **post** with placeholders.
Znai uses the [FreeMarker](https://freemarker.apache.org) template engine.

:include-file: templates/messages-http-call.md { autoTitle: true }

# Use A Pattern

    :include-template: templates/messages-http-call.md {method: "put"}

:include-template: templates/messages-http-call.md {method: "put"}

