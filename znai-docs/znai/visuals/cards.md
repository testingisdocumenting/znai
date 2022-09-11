# Image And Title

Use `card` fenced block plugin to render a card.

`````markdown
```card books.jpg {title: "My Card"}
Markdown content of the card goes here
* item one
* item two
* item three
```
`````

```card books.jpg {title: "My Card"}
Markdown content of the card goes here
* item one
* item two
* item three
```

# Multiple Columns

`````columns
left:
```card books.jpg {title: "My Card One"}
Markdown content of the card goes here
* item one
* item two
* item three
```

right:
```card books.jpg {title: "My Card Two"}
Markdown content of the card goes here
* item one
```
`````

# Code Snippets

Card plugin is designed to work with [code snippets](snippets/external-code-snippets)

`````markdown
```card castle.jpg {title: "Error Proof"}
Robust language and API
:include-file: snippets/file-name.js 
```
`````

```card castle.jpg {title: "Error Proof"}
Robust language and API
:include-file: snippets/file-name.js 
```

# API Parameters

Card plugin is designed to work with [API Parameters](snippets/api-parameters)

```````markdown

`````card castle.jpg {title: "API for the Win"}
Easy to use API
```api-parameters
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
``` 
`````
```````

`````card castle.jpg {title: "API for the Win"}
Easy to use API
```api-parameters
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
``` 
`````
