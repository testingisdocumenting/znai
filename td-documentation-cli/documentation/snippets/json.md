# Paths

Use `include-json` plugin to bring attention to a certain place in a json file. 

    :include-json: sample.json {paths: "root.key2.key22,root.key3.key31"}

Comma separated paths specified inside `paths` will be highlighted.

:include-json: sample.json {paths: "root.key2.key22,root.key3.key31"}

# Test Results

Consider leveraging testing frameworks to extract json samples from you end points. 
Information about what assertions were made can be used to highlight points of interest.
