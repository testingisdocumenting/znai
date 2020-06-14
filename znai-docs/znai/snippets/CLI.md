# Command Parameters

To bring attention to important parameters in your examples use the `cli-command` extension.

    :include-cli-command: my-super-command paramA --important-flag=true {paramsToHighlight: "important"}
    
:include-cli-command: my-super-command paramA --important-flag=true {paramsToHighlight: "important"}

Note: Parameter names gets matched as long as their names contain the passed value.

# Long Commands

Long command lines will be automatically wrapped into multiple lines.

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {paramsToHighlight: ["name", "value"]}

Use `threshold` parameter to specify the max length of a line before splitting:
 
```
:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    paramsToHighlight: ["name", "value"], 
    threshold: 30
}
```

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    paramsToHighlight: ["name", "value"], 
    threshold: 30
}

Use `splitAfter` to force line splitting after specified parameter:

```
:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    paramsToHighlight: ["name", "value"], 
    splitAfter: ["--score=2", "--value=8"]
}
```

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    paramsToHighlight: ["score", "value"], 
    splitAfter: ["--score=2", "--value=8"]
}

Note: Unlike `paramsToHighlight`, `splitAfter` must be an exact match

# Handle Special Symbols

If your command contains special symbols, such as `{`, move the command definition to a `command` option

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

You can highlight by a partial line text match:

    :include-cli-output: cli/file-path-of-captured.out {highlight: "remote"}

Or you can highlight by lines stored in a separate file:

    :include-cli-output: cli/file-path-of-captured.out {highlightPath: "cli/file-path-of-asserted-lines.txt"}

:include-file: cli/file-path-of-asserted-lines.txt {title: "cli/file-path-of-asserted-lines.txt"}

:include-cli-output: cli/file-path-of-captured.out {highlightPath: "cli/file-path-of-asserted-lines.txt"}

# ANSI Colors

CLI renders ANSI colors automatically.

:include-file: cli/ansi.out {title: "cli/ansi.out - file with ANSI sequence colors"}

    :include-cli-output: cli/ansi.out

:include-cli-output: cli/ansi.out

# Title

Use `title` to specify output of the output

    :include-cli-output: cli/file-path-of-captured.out {title: "Captured output"}

:include-cli-output: cli/file-path-of-captured.out {title: "Captured output"}

# Presentation Mode

In presentation mode, `cli-command` will simulate typing inside the terminal.

You can gradually reveal `cli-output` by providing `revealLineStop` parameter.
  
    :include-cli-output: cli/file-path-of-captured.out {revealLineStop: [0, 4]}
    
Passed `highlight` will highlight each line as a separate slide.

:include-cli-output: cli/file-path-of-captured.out {revealLineStop: [0, 4], highlight: "remote"}
