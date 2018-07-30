# Command parameters

To bring attention to important parameters in your examples use the `cli-command` extension.

    :include-cli-command: my-super-command paramA --important-flag=true {paramsToHighlight: "important"}
    
:include-cli-command: my-super-command paramA --important-flag=true {paramsToHighlight: "important"}

Note: Parameter names gets matched as long as their names contain the passed value.

# Long Commands

Long command lines will be automatically wrapped into multiple lines.

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {paramsToHighlight: ["name", "value"]}

# Handle Special Symbols

If your command contains special symbols like `{` then move command definition to `command` option

    :include-cli-command: {command: "another-command {file1} [file2]", paramsToHighlight: ["file1"]}

:include-cli-command: {command: "another-command {file1} [file2]", paramsToHighlight: ["file1"]}

# Output

To display console output and bring attention to certain lines use:

    :include-cli-output: file-path-of-captured.out {highlight: [0, 4]}

:include-cli-output: file-path-of-captured.out {highlight: [0, 4]}


# Presentation mode

In presentation mode, `cli-command` will simulate typing inside the terminal.

If your `cli-output` is long, you can split the presentation output into chunks:
  
    :include-cli-output: file-path-of-captured.out {highlight: [0, 4], chunkSize: 10, fadedSize: 2}
    
`chunkSize` specifies the maximum number of lines visible at a time.
 `fadedSize` specifies how many lines will be visible after and before as you move through output (default is 2).
    
:include-cli-output: file-path-of-captured.out {highlight: [0, 12], chunkSize: 10, fadedSize: 2}    





