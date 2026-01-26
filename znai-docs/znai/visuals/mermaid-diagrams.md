# Fenced Block

Use [Mermaid](https://mermaid-js.github.io/mermaid/#/) to create diagrams using text and code, written in a Markdown style.
Surround Mermaid expressions with a fenced block and specify `mermaid` as a language

    ```mermaid
    flowchart TD
        A[Start] --> B{Is it?}
        B -- Yes --> C[OK]
        C --> D[Rethink]
        D --> B
        B -- No ----> E[End]
    ```

The result will be a diagram.

```mermaid
flowchart TD
    A[Start] --> B{Is it?}
    B -- Yes --> C[OK]
    C --> D[Rethink]
    D --> B
    B -- No ----> E[End]
```

In presentation mode, rendered expressions will automatically scale to make use of the screen space.

Note: Rendering is done by using [Mermaid](https://mermaid-js.github.io/mermaid/#/) library.

# External File

Use include plugin to render a Mermaid diagram from a file.

    :include-mermaid: mermaid/class-diagram.mmd

:include-mermaid: mermaid/class-diagram.mmd

:include-file: mermaid/class-diagram.mmd { autoTitle: true }

# Wide Mode

Use `wide: true` to use as much horizontal space as required and available.

    ```mermaid {wide: true}
    sequenceDiagram
        par Alice to Bob
            Alice->>Bob: Go help John
        and Alice to John
            Alice->>John: I want this done today
            par John to Charlie
                John->>Charlie: Can we do this today?
            and John to Diana
                John->>Diana: Can you help us today?
        and Alice to Carl
            Alice->>Carl: I also want this done today
        end
    end
    ```

or 

```
:include-mermaid: mermaid/sequence-diagram.mmd { wide: true }
```

```mermaid {wide: true}
sequenceDiagram
    par Alice to Bob
        Alice->>Bob: Go help John
    and Alice to John
        Alice->>John: I want this done today
        par John to Charlie
            John->>Charlie: Can we do this today?
        and John to Diana
            John->>Diana: Can you help us today?
    and Alice to Carl
        Alice->>Carl: I also want this done today
    end
end
```
# Registering icon packs

Mermaid `architecture-beta` offers the possibility of displaying custom icons.

Use
```
mermaid {iconpacks : [{ name : "logos", url : "https://unpkg.com/@iconify-json/logos@1/icons.json" }]}
```
to register the `@iconify-json/logos@1` icon pack with the name `logos`.


Use
```
mermaid {iconpacks : [{ name : "logos", url : "../../icons.json" }]}
```
to register the `icons.json` icon pack with the name `logos`.

This assumes that 

- the `icons.json` file is checked in two directories below the current page.

- you have to include `icons.json` in the file `extensions.json` like this

```json {highlightValue: "root.additionalFilesToDeploy"}
{
  "cssResources": ["custom.css"],
  "jsResources": ["custom.js"],
  "htmlResources": ["custom.html"],
  "htmlHeadResources": ["tracking.html"],
  "additionalFilesToDeploy": ["icons.json"]
}
```


```mermaid {iconpacks : [{ name : "logos", url : "mermaid/demo_icons.json" }]}
architecture-beta
    group api(logos:aws-lambda)[API]

    service db(logos:aws-aurora)[Database] in api
    service disk1(logos:aws-glacier)[Storage] in api
    service disk2(logos:aws-s3)[Storage] in api
    service server(logos:aws-ec2)[Server] in api

    db:L <-[hosts]- R:server
    disk1:T <-[mounts]- B:server
    disk2:T <-[mounts]- B:db
```
