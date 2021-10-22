# Callout Comments

Use `commentsType: "inline"` to extract comments from a code and render them as bullet points

Given a file with comments

:include-file: callouts/file-name-with-comments.js

    :include-file: file-name-with-comments.js {commentsType: "inline"}

:include-file: callouts/file-name-with-comments.js {commentsType: "inline"}

Multiple comment lines can be put above a code line. All the comment lines will be merged and applied to the next code line.

:include-file: callouts/file-name-with-multiline-comments.py {title: "multi line comments above line"}

:include-file: callouts/file-name-with-multiline-comments.py {commentsType: "inline"}

# Spoilers

Set the `spoiler` property to initially hide explanations. It may be useful when teaching.

    :include-file: file-name-with-comments.js {commentsType: "inline", spoiler: true}

Click on the spoiler to reveal the explanations:

:include-file: callouts/file-name-with-comments.js {commentsType: "inline", spoiler: true}

# Remove Comments

Use `commentsType: "remove"` to hide all the comments from a code snippet
Given a file with comments

:include-file: callouts/file-name-with-comments.js

    :include-file: file-name-with-comments.js {commentsType: "remove"}

:include-file: callouts/file-name-with-comments.js {commentsType: "remove"}
