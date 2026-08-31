# Interactive Checklist

To render an interactive checklist use the `checkboxes` fence plugin.
Each bullet point inside becomes a checkbox users can tick.

    ```checkboxes
    * install cli
    * setup environment
    * run first example
    ```

```checkboxes
* install cli
* setup environment
* run first example
```

Note: Checked state is persisted in the browser local storage, so it survives page reloads and revisits.
Changing checklist content resets its checked state. During local preview state is not persisted.

# Complex Content

Bullet points can contain any markdown, including multiple paragraphs and code snippets.

    ````checkboxes
    * install dependencies using package manager

      ```bash
      npm install --save-dev my-tool
      ```

    * validate generated config

      ```javascript
      module.exports = {
        preset: "default",
      };
      ```

    * commit changes
    ````

````checkboxes
* install dependencies using package manager

  ```bash
  npm install --save-dev my-tool
  ```

* validate generated config

  ```javascript
  module.exports = {
    preset: "default",
  };
  ```

* commit changes
````

Note: Four backticks were used instead of the usual three. This is done so one fence block can include
another block (code snippet) without confusion. It can be any number of backticks greater than three.
