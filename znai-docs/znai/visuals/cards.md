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

Note: Card scales down large image to fit

# Image Height And Background

When you use icon like images, e.g. SVGs, then they will take all the horizontal available space, and it may not be ideal.

Use `imageHeight` to force image height. Use `imageBackground` to specify a [background color/gradient](https://www.w3schools.com/css/css3_gradients.asp).

`````markdown
```card diamond.svg {title: "My Card", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(29 41 41), rgb(145, 152, 229))"}
Markdown content of the card goes here
* item one
* item two
* item three
```
`````

```card diamond.svg {title: "My Card", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(29 41 41), rgb(145, 152, 229))"}
Markdown content of the card goes here
* item one
* item two
* item three
```

# Multiple Columns

Use [Colums](layout/columns) to arrange cards side by side

```````columns
left:
```card diamond.svg {title: "Card Title", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(29 41 41), rgb(145, 152, 229))"}
Markdown content goes here

:include-file: snippets/file-name.js 
```

middle:
```card small-book.png {title: "Book", imageHeight: 120}
Markdown content of the card goes here
* item one
* item two
* item three
```

right:
`````card star.svg {title: "API for the Win", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(154 128 145 / 0%), rgb(255 206 206))"}
Easy to use API
```api-parameters
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```
`````
````````

`````````markdown
```````columns
left:
```card diamond.svg {title: "Card Title", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(29 41 41), rgb(145, 152, 229))"}
Markdown content goes here

:include-file: snippets/file-name.js 
```

middle:
```card small-book.png {title: "Book", imageHeight: 120}
Markdown content of the card goes here
* item one
* item two
* item three
```

right:
`````card star.svg {title: "API for the Win", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(154 128 145 / 0%), rgb(255 206 206))"}
Easy to use API
```api-parameters
firstName, String, description with *markdown* support
score, Integer, another description line with *markdown* support
```
`````
````````
`````````

Note: Card plugin is designed to work with [code snippets](snippets/external-code-snippets) and with [API Parameters](snippets/api-parameters) by reducing spacing integrating border lines

# Links 

Card plugin automatically converts links at the end of the fenced block content into dedicated card links.

```card diamond.svg {title: "Card Title", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(29 41 41), rgb(145, 152, 229))"}
Markdown content goes here

:include-file: snippets/file-name.js 

[Learn More](snippets/external-code-snippets)
```

`````markdown
```card diamond.svg {title: "Card Title", imageHeight: 120, imageBackground: "linear-gradient(to right, rgb(29 41 41), rgb(145, 152, 229))"}
Markdown content goes here

:include-file: snippets/file-name.js 

[Learn More](snippets/external-code-snippets)
```
`````
