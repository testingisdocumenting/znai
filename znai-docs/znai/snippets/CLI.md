# Parameters Highlight

To bring attention to important parameters in your CLI examples use the `cli` fence plugin.

    ```cli {highlight: "important"}
    my-super-command paramA --important-flag=true 
    ```

```cli {highlight: "important"}
my-super-command paramA --important-flag=true 
```

Note: Parameter names gets matched as long as their names contain the passed value.

````markdown {title: "stacking multiple commands"} 
```cli {highlight: "important"}
my-super-command paramA --important-flag=true 
another-command stop  
```
````

```cli {highlight: "important"} 
my-super-command paramA --important-flag=true 
another-command stop  
```

# Include Plugin 

Alternatively to fence plugin above, you can use include type.
Note: options listed below are applicable to either form

    :include-cli-command: my-super-command paramA --important-flag=true {highlight: "important"}
    
:include-cli-command: my-super-command paramA --important-flag=true {highlight: "important"}

# Long Commands

Long command lines will be automatically wrapped into multiple lines.

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {highlight: ["name", "value"]}

Use `threshold` parameter to specify the max length of a line before splitting:
 
```
:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    highlight: ["name", "value"], 
    threshold: 30
}
```

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    highlight: ["name", "value"], 
    threshold: 30
}

Use `splitAfter` to force line splitting after specified parameter:

```
:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    highlight: ["name", "value"], 
    splitAfter: ["--score=2", "--value=8"]
}
```

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {
    highlight: ["score", "value"], 
    splitAfter: ["--score=2", "--value=8"]
}

Note: Unlike `highlight`, `splitAfter` must be an exact match

# Handle Special Symbols

If your command contains special symbols, such as `{`, move the command definition to a `command` option

    :include-cli-command: {command: "another-command {file1} [file2]", highlight: ["file1"]}

:include-cli-command: {command: "another-command {file1} [file2]", highlight: ["file1"]}

# From File

You can read a command from file.

    :include-cli-command: {commandFile: "cli/command.txt", highlight: "value"}

:include-file: cli/command.txt {title: "cli/command.txt"}

:include-cli-command: {commandFile: "cli/command.txt", highlight: "value"}

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

# Extract Snippets

Use `startLine`, `endLine` to extract specific content by using marker lines.

    :include-cli-output: cli/file-path-of-captured.out {
        title: "Limited captured output",
        startLine: "git push",
        endLine: "master -> master"    
    }
    
:include-cli-output: cli/file-path-of-captured.out {
    title: "Limited captured output",
    startLine: "git push",
    endLine: "master -> master"    
}

More on snippets extractions: [Snippets Manipulation](snippets/snippets-manipulation).

# Presentation Mode

In presentation mode, cli command related plugins will simulate typing inside the terminal.

You can gradually reveal `cli-output` by providing `revealLineStop` parameter.
  
    :include-cli-output: cli/file-path-of-captured.out {revealLineStop: [0, 4]}
    
Passed `highlight` will highlight each line as a separate slide.

:include-cli-output: cli/file-path-of-captured.out {revealLineStop: [0, 4], highlight: "remote"}
