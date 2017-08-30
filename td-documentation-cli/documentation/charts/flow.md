# Nodes and Edges

:include-flow-chart: simple-dag.json

Specify a `JSON` file to define a flow chart.  

:include-file: simple-dag.json    

`nodes` and `edges` is the minimum information you need to provide.

    :include-flow-chart: simple-dag.json
    

# Highlight

:include-file: highlight-dag.json     

Use `highlight` to highlight a node

:include-flow-chart: highlight-dag.json

Or use `highlight` property of a `flow-chart` include plugin

    :include-flow-chart: simple-dag.json {highlight: "n3"}
    
:include-flow-chart: simple-dag.json {highlight: "n3"}

Note: to highlight more than one element use `{highlight: ["n3", "n4"]}`

# Links
  
To attach links to nodes use `url` property
            
:include-flow-chart: links-dag.json     
        
Combine links and highlights to create a sub navigation for your product.
         
:include-file: links-dag.json     
            


