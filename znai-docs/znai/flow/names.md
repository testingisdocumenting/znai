---
title: Page Titles
---

# Automatic Names

By default, page names are automatically derived from file names. `file-name.md` has the page title `File Name`.

# Name Overrides

To override the default, add the following syntax to the top of your Markdown file: 

    ---
    title: Custom Name
    ---
    
To keep things easy for future documentation owners, it's good practice to keep page and file names the same. In some cases, however, you want to make exceptions, for example, in cases where you:
* Need to use special characters in title
* Want to avoid auto-capitalization

