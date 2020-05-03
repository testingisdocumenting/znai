# LATEX

To render math you can use [LaTeX](https://en.wikibooks.org/wiki/LaTeX/Mathematics) math expressions. 
Surround LaTeX expression with fenced block and specify `latex` as a language

    ```latex
    \forall x \in X, \quad \exists y \leq \epsilon
    ```

The result will be a following math expression.

```latex
\forall x \in X, \quad \exists y \leq \epsilon
```

In presentation mode, rendered expressions will automatically scale to make use of the screen space.

Note: Rendering is done by using [KaTeX](https://github.com/Khan/KaTeX) library.
And is using [KaTeX](https://github.com/Khan/KaTeX) fonts.  