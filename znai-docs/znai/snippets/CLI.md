---
identifier: {validationPath: "org/testingisdocumenting/znai/extensions/file/SnippetHighlightFeature.java"}
---

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

# ANSI Colors Output

CLI renders ANSI colors automatically.

:include-file: cli/ansi.out {title: "cli/ansi.out - file with ANSI sequence colors"}

    :include-cli-output: cli/ansi.out

:include-cli-output: cli/ansi.out

# Title

Use `title` to specify output of the output

    :include-cli-output: cli/ansi.out {title: "captured output"}

:include-cli-output: cli/ansi.out {title: "captured output"}

# Anchor

When you specify a title, hover mouse over it to see a clickable anchor.

Use `anchorId` to override auto generated identifier.

    :include-cli-output: cli/ansi.out {
      title: "captured output",
      anchorId: "my-output"
    }

:include-cli-output: cli/ansi.out {
  title: "captured output",
  anchorId: "my-output"
}

# Output Highlight

Use `:identifier: highlight` to highlight lines

    :include-cli-output: cli/ansi.out {
      title: "captured output",
      "highlight": "GET https"
    }

:include-cli-output: cli/ansi.out {
  title: "captured output",
  "highlight": "GET https"
}

Use `:identifier: highlightPath` to highlight lines based on the content of a file

    :include-cli-output: cli/ansi.out {highlightPath: "cli/file-path-of-asserted-lines.txt"}

:include-file: cli/file-path-of-asserted-lines.txt {autoTitle: true}

:include-cli-output: cli/ansi.out {highlightPath: "cli/file-path-of-asserted-lines.txt"}


# Wide Mode

Use `wide` option to occupy as much horizontal space as available

    :include-cli-output: cli/wide-output.out {
      title: "Captured output",
      wide: true
    }

:include-cli-output: cli/wide-output.out {
  title: "Captured output",
  wide: true
}

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

    :include-cli-output: cli/file-path-of-captured.out {revealLineStop: [0, 4], highlight: "remote"}
    
Passed `:identifier: highlight` will highlight each line as a separate slide.

:include-cli-output: cli/file-path-of-captured.out {revealLineStop: [0, 4], highlight: "remote"}
