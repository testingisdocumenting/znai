# Speed Bump

People skim through documentation. You can grab users attention by using attention signs. 

To create an attention sign, start a paragraph with one of the predefined keywords followed by a colon.

    Keyword: message

# Note

    Note: It is very important to not overuse signs. Make sure each sign counts.

Note: It is very important to not overuse signs. Make sure each sign counts.

# Warning
    
    Warning: Bring attention to a common mistake or an often missed configuration step 
    using a warning sign. Do not use too many warning signs.

Warning: Bring attention to a common mistake or an often missed configuration 
step using a warning sign. Do not use too many warning signs.

# Question

    Question: Use the question sign to bring an extra attention to the main idea of a page.
    \
    What is the point of the `attention signs`?

Question: Use the question sign to bring extra attention to the main idea of a page.
\
What is the point of the `attention signs`?

# Exercise

    Exercise: write a hello world example in this language

Exercise: write a hello world example in this language

# Avoid

    Avoid: using multiple versions of `ReactJS` inside one project.

Avoid: using multiple versions of `ReactJS` inside one project.

# Do Not

    Don't: commit node_modules to your repository

    Do not: commit node_modules to your repository

Don't: commit node_modules to your repository

Do not: commit node_modules to your repository

# Tip

    Tip: use temporary directory to generate the summary file for upload

Tip: use temporary directory to generate the summary file for upload

# Recommendation

    Recommendation: write automated tests for new business logic
    
Recommendation: write automated tests for new business logic

# Fence Block

Use fence block to create an explicit attention block. 

`````markdown
```attention-note
hello world
```
`````

```attention-note
hello world
```

By default, there is only icon. Use `label` to add text

`````markdown
```attention-note {label: "Custom Label"}
hello world
```
`````

```attention-note {label: "Custom Label"}
hello world
```

Using block makes it easier to include other plugins inside

```````markdown
`````attention-note
Use this command to setup fresh environment
```cli
mycommand setup
```
`````
```````

`````attention-note
Use this command to setup fresh environment
```cli
mycommand setup
```
`````

# Attention Block Types

```
attention-<type>
```

```attention-note
`note`
```

```attention-warning
`warning`
```

```attention-avoid
`avoid`
```

```attention-question
`question`
```

```attention-recommendation
`recommendation`
```

# Custom Attention Block

Use `attention-custom` when the built-in types are not enough. The free form parameter defines
the type. It is used as a CSS class name, exactly like `note`, `warning`, and the other built-in types.


`````markdown
```attention-custom my-type
hello world
```
`````

```attention-custom my-type
hello world
```

`attention-custom` only provides the markup placeholders. Each guide is responsible for implementing
the CSS for its own types. 

Use in combination with `style.css`. Scope rules under `.theme-znai-dark` to define dark mode colors.

:include-file: attention-custom.css {title: "style.css"}

# Custom Icon

Unlike the built-in types, a custom type has no icon by default. Use the `icon` parameter to display one.

To pick an icons to use go to [Feather icons](https://feathericons.com/).
`````markdown
```attention-custom my-type {icon: "zap"}
hello world
```
`````

```attention-custom my-type {icon: "zap"}
hello world
```

Combine `icon` with the optional `label`

`````markdown
```attention-custom my-type {icon: "zap", label: "Consider"}
hello world
```
`````

```attention-custom my-type {icon: "zap", label: "Consider"}
hello world
```
