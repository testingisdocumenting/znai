# Plant Uml

Draw sequence diagrams with [PlantUml](http://plantuml.com/sequence-diagram)

`````markdown-and-result
```plantuml
@startuml
Alice -> Bob: Authentication Request
Bob --> Alice: Authentication Response

Alice -> Bob: Another authentication Request
Alice <-- Bob: another authentication Response
@enduml
```
`````
\
Consider using [Columns Layout](features/columns) to put your story and a diagram side by side