# Definition

To define a footnote use

`````
[^my-id]: extra content for my footnote goes here
    potentially including code blocks
    ```
    Constructor()
    ```
`````

To add a reference to the footnote use `[^my-id]` which will result in [^my-id]

[^my-id]: extra content for my footnote goes here
    potentially including code blocks
    ```
    Constructor()
    ```

Note: numeric footnotes are treated as text footnotes, and they will be assigned the auto incremented
number in order of appearance.

# Preview

Hover mouse over a footnote reference see its content in a tooltip. Click on it to go the footnotes list at the 
bottom of the page.

# Footnotes Inside Attention Blocks

Footnotes can be used inside different content blocks like `attention-note`:

    ```attention-warning
    Use something important [^my-id]
    ```

```attention-warning
Use something important [^my-id]
```

# Footnotes List

A footnotes list is automatically appended at the end of the page when footnotes are present.
Clicking the number in the list navigates back to the reference [^another-note].

[^another-note]: This is another footnote to demonstrate the list with multiple entries.

To hide the footnotes list, set `hideFootnoteList` to `true` in `meta.json`:

```json {title: "meta.json"}
{
  "hideFootnoteList": true
}
```
