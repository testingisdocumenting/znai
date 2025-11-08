# Context For LLMs

Note: Initial experimental support

Znai generates a single markdown file called `llm.txt` to be used by LLMs. 
It is deployed at the root of the documentation and can be used in multiple scenarios. 
The primary use case is to provide guide context to systems like Claude Code.

# Generate During Build To Keep In Repository

`````tabs
CLI: 
```cli {highlight: "llm-txt"}
znai build --llm-txt-output-path full-guide.md
```

Maven: :include-file: maven-plugin-llm.xml {title: "expose llm.txt to source tree", highlight: "llmTxt"} 
`````

