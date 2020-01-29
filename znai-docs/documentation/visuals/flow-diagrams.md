# Nodes and Edges

:include-flow-chart: flow-diagrams/simple-dag.json

Specify a `JSON` file to define a flow chart.  

:include-file: flow-diagrams/simple-dag.json {title: "simple-dag.json"}

At the minimum `edges` must be provided.

    :include-flow-chart: simple-dag.json
    
# Multiline Labels

Use `\n` to split your label into multiple lines.

:include-file: flow-diagrams/multiline-dag.json

:include-flow-chart: flow-diagrams/multiline-dag.json

# Nodes Size

Add `config` section to specify a common size for all the nodes 

:include-json: flow-diagrams/common-size-dag.json {title: "shared nodes size", collapsedPaths: ['root.nodes', 'root.edges']}

:include-flow-chart: flow-diagrams/common-size-dag.json

Note: Size unit is not based on pixels. It is sized in inches (assuming correct DPI). It is the size units used by [Graphviz](https://graphviz.gitlab.io/))

Use `width` and `height` property on a node itself to control individual nodes size:

:include-json: flow-diagrams/common-size-with-override-dag.json {title: "nodes size override", collapsedPaths: ['root.config', 'root.edges']}

:include-flow-chart: flow-diagrams/common-size-with-override-dag.json

# Highlight

:include-file: flow-diagrams/highlight-dag.json     

Use `highlight` to highlight a node.

:include-flow-chart: flow-diagrams/highlight-dag.json

Or use `highlight` property of a `flow-chart` include plugin.

    :include-flow-chart: simple-dag.json {highlight: "n3"}
    
:include-flow-chart: flow-diagrams/simple-dag.json {highlight: "n3"}

Note: To highlight more than one element use `{highlight: ["n3", "n4"]}`

# Color Groups

Use `colorGroup` to assign a color group to a node. There are four color groups out of the box: `a` (default), `b`, `c`, `d`.

:include-file: flow-diagrams/simple-dag-colors.json     

:include-flow-chart: flow-diagrams/simple-dag-colors.json

# Shapes

Use `shape` to assign one of the predefined shapes.

:include-file: flow-diagrams/simple-dag-shapes.json

:include-flow-chart: flow-diagrams/simple-dag-shapes.json

# Legend

Use `include-diagram-legend` to add a legend to your diagram. Optionally add `clickableNodes: true` to insert a message
that the nodes are clickable (in case you use urls).

    :include-diagram-legend: {a: "inputs", b: "optimization process", c: "outcome", d: "unknown",
        clickableNodes: true}

    :include-flow-chart: flow-diagrams/simple-dag-colors.json

:include-diagram-legend: {a: "inputs", b: "optimization process", c: "outcome", d: "unknown",
    clickableNodes: true}

:include-flow-chart: flow-diagrams/simple-dag-colors.json


# Presentation

In presentation mode nodes will be highlighted one at a time.

To force all highlights to appear at once add this before (either in the same section, or at the start of a document).

    :include-meta: {allAtOnce: true}

# Vertical Layout

To switch layout from horizontal to vertical use `vertical: true`.

:include-flow-chart: flow-diagrams/simple-dag.json {vertical: true}

# Layout Types

Use `layout` to specify a different underlying layout engine. Only `dot` (default) and `neato` is 
supported at the moment.

    :include-flow-chart: simple-dag.json {layout: "neato"}

:include-flow-chart: flow-diagrams/simple-dag.json {layout: "neato"}

[Neato layout guide from Graphviz](https://www.graphviz.org/pdf/neatoguide.pdf)

# Links
  
To attach links to nodes use `url` property.
            
:include-flow-chart: flow-diagrams/links-dag.json     
        
Combine links and highlights to create a sub navigation for your product.
         
:include-file: flow-diagrams/links-dag.json     

# Reusable Nodes

Move node definitions to a one or more separate files if you use them across multiple diagrams.

:include-file: flow-diagrams/graph-using-lib.json {title: "only edges definitions"}


:include-file: flow-diagrams/nodes-lib-a.json {title: "nodes/set-a.json"}
:include-file: flow-diagrams/nodes-lib-b.json {title: "nodes/set-b.json"}

    :include-flow-chart: flow-diagrams/graph-using-lib.json {nodeLibPath: "nodes/set-a.json"}

:include-flow-chart: flow-diagrams/graph-using-lib.json {nodeLibPath: "flow-diagrams/nodes-lib-a.json"}

    :include-flow-chart: flow-diagrams/graph-using-lib.json {nodeLibPath: ["nodes/set-a.json", "nodes/set-b.json"]}

:include-flow-chart: flow-diagrams/graph-using-lib.json 
    {nodeLibPath: ["flow-diagrams/nodes-lib-a.json", "flow-diagrams/nodes-lib-b.json"]}

