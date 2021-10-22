# Markdown

`Znai` uses [Markdown](https://commonmark.org), a markup language that uses ASCII to represent styles and page structure.

```markdown-and-result
## Sub Header

Normal paragraph text. Some *italic* and **bold**.
Followed by bullet points:
* Apple
* Banana
* Water
```

# Extensions

Znai has extensions mechanism that are not part of standard markdown.

## Embed External File 

```markdown-and-result
## Server Configuration

:include-file: config/server.yml {autoTitle: true}
```

## Graphvis Diagram

```columns
left:
    :include-graphviz: visuals/graphviz.dot {autoTitle: true}

:include-file: visuals/graphviz.dot {autoTitle: true}

right:
:include-graphviz: visuals/graphviz.dot
```

# Website

This user guide is built using `znai`. Continue reading to learn the features that may improve your users experience
and reduce your maintenance burden. 