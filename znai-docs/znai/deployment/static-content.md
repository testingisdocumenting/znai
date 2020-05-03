# Build 

To generate static content for deployment use:

:include-cli-command: znai --doc-id my-docs --deploy /path/to/static-content

Using Maven:

:include-file: maven-deploy.xml {title: "maven plugin", highlight: "deployRoot"}

:include-cli-command: mvn znai:build
