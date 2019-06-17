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

# From File

You can read a command from file.

    :include-cli-command: {commandFile: "cli/command.txt", paramsToHighlight: "value"}

:include-file: cli/command.txt {title: "cli/command.txt"}

:include-cli-command: {commandFile: "cli/command.txt", paramsToHighlight: "value"}

This option is useful for displaying a captured command during tests. 

# Output

To display console output and bring attention to certain lines use:

    :include-cli-output: cli/file-path-of-captured.out {highlight: [0, 4]}

:include-cli-output: cli/file-path-of-captured.out {highlight: [0, 4]}

You can highlight by a partial line text match

    :include-cli-output: cli/file-path-of-captured.out {highlight: "remote"}

Or you can highlight by lines stored in a separate file

    :include-cli-output: cli/file-path-of-captured.out {highlightFile: "cli/file-path-of-asserted-lines.txt"}

:include-file: cli/file-path-of-asserted-lines.txt {title: "cli/file-path-of-asserted-lines.txt"}

:include-cli-output: cli/file-path-of-captured.out {highlightFile: "cli/file-path-of-asserted-lines.txt"}


# Presentation mode

In presentation mode, `cli-command` will simulate typing inside the terminal.

If your `cli-output` is long, you can split the presentation output into chunks:
  
    :include-cli-output: cli/file-path-of-captured.out {highlight: [0, 4], chunkSize: 10, fadedSize: 2}
    
`chunkSize` specifies the maximum number of lines visible at a time.
 `fadedSize` specifies how many lines will be visible after and before as you move through output (default is 2).
    
:include-cli-output: cli/file-path-of-captured.out {highlight: [0, 12], chunkSize: 10, fadedSize: 2}    





