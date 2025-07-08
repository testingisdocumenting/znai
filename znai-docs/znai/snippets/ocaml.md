---
title: "OCaml"
---

# Extract Comment Text

Given text file:
:include-file: ocaml/domain.mli {autoTitle: true}

When you use the following plugin, it will extract the comment and embed it as if it was typed.
```
:include-ocaml-comment: ocaml/domain.mli {commentLine: "description that contains"}
```

:include-ocaml-comment: ocaml/domain.mli {commentLine: "description that contains"}
