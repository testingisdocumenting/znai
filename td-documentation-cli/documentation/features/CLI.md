# Command parameters

To bring attention to important parameters in your examples use `cli-command` extension.

    :include-cli-command: my-super-command paramA --important-flag=true {paramsToHighlight: "important"}
    
:include-cli-command: my-super-command paramA --important-flag=true {paramsToHighlight: "important"}

Note: parameter name gets matched as long as its name contains passed value

Use plural version `paramsToHighlight` to highlight multiple parameters
 
    :include-cli-command: another-command --score=2 --name=Name --value=8 {paramsToHighlight: ["name", "value"]}

:include-cli-command: another-command --score=2 --name=Name --value=8 {paramsToHighlight: ["name", "value"]}

# Long Commands

Long command lines will be automatically wrapped into multiple lines

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {paramsToHighlight: ["name", "value"]}

# Output

To display console output and bring attention to certain lines use 

    :include-cli-output: file-path-of-captured.out {highlight: [0, 4]}

:include-cli-output: file-path-of-captured.out {highlight: [0, 4]}


# Presentation mode

In presentation mode `cli-command` will simulate typing inside terminal.

In case `cli-output` is long, you can split presentation output into chunks. 
  
    :include-cli-output: file-path-of-captured.out {highlight: [0, 4], chunkSize: 10, fadedSize: 2}
    
`chunkSize` specifies the maximum number of lines visible at a time.
 `fadedSize` specifies how many lines will be visible after and before as you move through output (default is 2).
    
:include-cli-output: file-path-of-captured.out {highlight: [0, 12], chunkSize: 10, fadedSize: 2}    





