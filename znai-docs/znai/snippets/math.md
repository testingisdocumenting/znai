# LaTeX Block

To render math you can use [LaTeX](https://en.wikibooks.org/wiki/LaTeX/Mathematics) math expressions. 
Surround LaTeX expression with fenced block and specify `latex` as a language

    ```latex
    \forall x \in X, \quad \exists y \leq \epsilon
    ```

The result will be a following math expression.

```latex
\forall x \in X, \quad \exists y \leq \epsilon
```
    
    ```latex
    x = \begin{cases}
    a &\text{if } b \\
    c &\text{if } d \\
    x &\text{if } z
    \end{cases}
    ```

```latex
x = \begin{cases}
a &\text{if } b \\
c &\text{if } d \\
x &\text{if } z
\end{cases}
```

# LaTeX Inline

Znai also supports inline LaTeX. Use single backticks instead of three to render math expressions inline. JSON parameters are used to allow LaTeX braces to be parsed correctly.

    It holds that `:latex: {src: "\\frac{1}{2} < \\sqrt{2}"}`.

The result will be a following math expression.

It holds that `:latex: {src: "\\frac{1}{2} < \\sqrt{2}"}`.

# Presentation Mode

In presentation mode, rendered expressions will automatically scale to make use of the screen space.

# KaTex

Rendering is done by using awesome [KaTeX](https://github.com/Khan/KaTeX) library.

[KaTeX](https://github.com/Khan/KaTeX) fonts are copied to generated documentation resources.  
