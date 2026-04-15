# Primary Use Case

When your documentation covers the same concepts across multiple languages or platforms,
you can use `tab-content` to make the entire page switchable. Unlike regular `tabs` which require
wrapping each difference in a tabs fence, `tab-content` lets you sprinkle tab-specific content
throughout the page. Shared content stays as regular markdown.

This page itself uses `tab-content` — notice the tabs at the top. Switch between them to see
how content changes across sections while shared text remains.

Below is a demo, except the final [Definition](#definition) section.

# Project Setup

Every project needs an editor configuration. Create a workspace settings file in your project root.

`````tab-content "VS Code"
Create `.vscode/settings.json` in your project root:

```json
{
  "editor.formatOnSave": true,
  "editor.tabSize": 4,
  "files.trimTrailingWhitespace": true
}
```
`````

`````tab-content Emacs
Create `.dir-locals.el` in your project root:

```elisp
((nil . ((indent-tabs-mode . nil)
         (tab-width . 4)
         (fill-column . 100))))
```
`````

`````attention-note
Double check these settings.

```tab-content Emacs
Make sure `indent-tabs-mode` is set to `nil` to avoid mixing tabs and spaces.
```

```tab-content "VS Code"
Make sure `editor.tabSize` matches the value used by your team's linter.
```
`````

# Build Configuration

Set up the build tool for your project. All editors use the same underlying build system,
but the integration differs.

`````tab-content "VS Code"
Create `.vscode/tasks.json` to integrate the build:

````tabs
Java:
```json
{
  "version": "2.0.0",
  "tasks": [{
    "label": "build",
    "type": "shell",
    "command": "mvn compile",
    "group": "build"
  }]
}
```

C++:
```json
{
  "version": "2.0.0",
  "tasks": [{
    "label": "build",
    "type": "shell",
    "command": "cmake --build build/",
    "group": "build"
  }]
}
```
````

Use `Ctrl+Shift+B` to run the build task.
`````

`````tab-content Emacs
Configure `compile-command` in `.dir-locals.el`:

````tabs
Java:
```elisp
((nil . ((compile-command . "mvn compile"))))
```

C++:
```elisp
((nil . ((compile-command . "cmake --build build/"))))
```
````

Use `M-x compile` to run the build.
`````

# Debugging

All projects benefit from proper debug configuration. Breakpoints, watch expressions,
and stepping through code work the same conceptually — but the configuration differs per editor.

`````tab-content "VS Code"
Create `.vscode/launch.json`:

````tabs
Java:
```json
{
  "version": "0.2.0",
  "configurations": [{
    "type": "java",
    "name": "Debug Main",
    "request": "launch",
    "mainClass": "com.example.Main"
  }]
}
```

C++:
```json
{
  "version": "0.2.0",
  "configurations": [{
    "type": "cppdbg",
    "name": "Debug Main",
    "request": "launch",
    "program": "${workspaceFolder}/build/main"
  }]
}
```
````

Press `F5` to start debugging.
`````

`````tab-content Emacs
````tabs
Java:
Use `dap-mode` with the Java debug adapter:

```elisp
(require 'dap-java)
(dap-register-debug-template "Debug Main"
  (list :type "java"
        :request "launch"
        :mainClass "com.example.Main"))
```

C++:
Use `dap-mode` with `gdb`:

```elisp
(require 'dap-gdb-lldb)
(dap-register-debug-template "Debug Main"
  (list :type "cppdbg"
        :request "launch"
        :program "${workspaceFolder}/build/main"))
```
````

Use `M-x dap-debug` to start a debug session.
`````

# Definition

Use `tab-content` fence blocks with a tab id to mark sections of content that are specific to a tab.
Content outside of `tab-content` blocks is always visible regardless of which tab is selected.

```````markdown
Shared content here is always visible.

```tab-content "VS Code"
Press `F5` to start debugging.
```


```tab-content Emacs
Use `M-x dap-debug` to start a debug session.
```

More shared content here.
```````

When a page contains `tab-content` blocks, tabs automatically appear at the top of the page.

Regular `tabs` fence blocks can be used inside `tab-content` for additional sub-grouping.
The global page tabs and regular tabs operate independently.

```````markdown
Shared content here is always visible.

`````tab-content "VS Code"
```tabs
Java: hello VS Code Java
C++: hello Vs Code C++
```
`````


`````tab-content Emacs
```tabs
Java: hello Emacs Java
C++: hello Emacs C++
```
`````

More shared content here.
```````

---

Shared content here is always visible.

`````tab-content "VS Code"
```tabs
Java: hello VS Code Java
C++: hello Vs Code C++
```
`````


`````tab-content Emacs
```tabs
Java: hello Emacs Java
C++: hello Emacs C++
```
`````

More shared content here.
