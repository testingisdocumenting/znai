# Nodes and Edges

:include-flow-chart: simple-dag.json

Specify a `JSON` file to define a flow chart.  

:include-file: simple-dag.json {title: "simple-dag.json"}

At minimum, `nodes` and `edges` must be provided.

    :include-flow-chart: simple-dag.json
    

# Highlight

:include-file: highlight-dag.json     

Use `highlight` to highlight a node.

:include-flow-chart: highlight-dag.json

Or use `highlight` property of a `flow-chart` include plugin.

    :include-flow-chart: simple-dag.json {highlight: "n3"}
    
:include-flow-chart: simple-dag.json {highlight: "n3"}

Note: To highlight more than one element use `{highlight: ["n3", "n4"]}`

# Presentation

In presentation mode nodes will be highlighted one at a time.

To force all highlights to appear at once add this before (either in the same section, or at the start of a document).

    :include-meta: {allAtOnce: true}

# Vertical Layout

To switch layout from horizontal to vertical use `vertical: true`.

:include-flow-chart: simple-dag.json {vertical: true}

# Layout Types

Use `layout` to specify a different underlying layout engine. Only `dot` (default) and `neato` is 
supported at the moment.

    :include-flow-chart: simple-dag.json {layout: "neato"}

:include-flow-chart: simple-dag.json {layout: "neato"}

[Neato layout guide from Graphviz](https://www.graphviz.org/pdf/neatoguide.pdf)

# Links
  
To attach links to nodes use `url` property.
            
:include-flow-chart: links-dag.json     
        
Combine links and highlights to create a sub navigation for your product.
         
:include-file: links-dag.json     
            


