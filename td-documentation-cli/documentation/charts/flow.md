# Nodes and Edges

Flow chart is defines as `JSON` inside a file.  

:include-file: simple-dag.json    

`nodes` and `edges` is the minimum information you need to provide.

    :include-flow-chart: simple-dag.json
    
:include-flow-chart: simple-dag.json

# Highlight

:include-file: highlight-dag.json     

Use `highlight` to highlight a node

:include-flow-chart: highlight-dag.json

Or use `highlight` property of a `flow-chart` include plugin

    :include-flow-chart: simple-dag.json {highlight: "n3"}
    
:include-flow-chart: simple-dag.json {highlight: "n3"}

Note: to highlight more than one element use `{highlight: ["n3", "n4"]}`
    

