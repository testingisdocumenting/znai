# Overview

`include-javascript-function` delegates rendering of a block to a plain
JavaScript function. znai creates a DOM node and hands your function:

* the DOM node to render into
* the parameters from markdown (as `args`)
* a `themeObservable` so rendering can react to light/dark theme switches

```markdown
:include-javascript-function: themeBox {
  label: "my custom content box",
  customKey: "custom value",
}
```

:include-javascript-function: themeBox {
  label: "my custom content box",
  customKey: "custom value"
}

See the [sample function](#sample-function) at the end of this page.

# Registering A Function

Attach your function to `window` under the name you want to reference, and
load the script via [`extensions.json`](configuration/extensions):

```javascript {title: "theme-box.js"}
(function () {
    window.themeBox = function (node, args, themeObservable) {
        // draw inside `node`
        // read initial theme via `themeObservable.current`
        // subscribe to updates via `themeObservable.subscribe(listener)`
    };
})();
```

```json {title: "extensions.json"}
{
  "jsResources": ["plugins/javascript/theme-box.js"]
}
```

# Function Signature

Your function receives three arguments:

* `node` — the parent `div` to append content to.
* `args` — the parameters passed from markdown. Framework-level keys (`title`,
  `wide`, `className`, `anchorId`) are handled by znai and not forwarded.
* `themeObservable` — live access to the current znai theme.

```javascript {title: "themeObservable shape"}
{
    current: "light" | "dark",
    subscribe(listener) {
        // listener is called with the new theme name on every change
    }
}
```

The function may return a cleanup function. znai calls it when the block is
removed or re-rendered — dispose timers, external listeners, or other
resources here.

The [sample function](#sample-function) below returns a cleanup that logs to
the browser console:

:include-file: javascript/theme-box.js {
  title: "theme-box.js",
  surroundedByScope: {start: "function cleanup", scope: "{}"}
}

Open DevTools, then navigate away from this page — every `themeBox` rendered
above is unmounted and its cleanup runs.

# Theme Toggle

:include-javascript-function: themeBox {
    message: "Switch the theme to see me update.",
    tags: ["demo", "theme"]
}

Toggle the theme using the switcher in the bottom left corner and watch the
custom content restyle. The hookup is one line:

```javascript
themeObservable.subscribe(applyTheme);
```

# Title

Pass `title` to render a title bar above the block. znai owns the title via
its shared container, so your function never sees it.

```markdown
:include-javascript-function: themeBox {
    title: "theme aware box",
    message: "rendered inside a titled container"
}
```

:include-javascript-function: themeBox {
    title: "theme aware box",
    message: "rendered inside a titled container"
}

# Wide

Pass `wide: true` to span the full page width, matching wide images and
iframes.

```markdown
:include-javascript-function: themeBox {
    title: "wide theme aware box",
    wide: true,
    note: "this block breaks out of the single-column width and spans edge to edge of the page container, giving long values plenty of horizontal room to stretch before they need to wrap onto a second line",
    tags: ["wide", "demo", "edge-to-edge", "theme"],
    metrics: {"requests": 1024, "latency_ms": 42, "error_rate": 0.003}
}
```

:include-javascript-function: themeBox {
    title: "wide theme aware box",
    wide: true,
    note: "this block breaks out of the single-column width and spans edge to edge of the page container, giving long values plenty of horizontal room to stretch before they need to wrap onto a second line",
    tags: ["wide", "demo", "edge-to-edge", "theme"],
    metrics: {"requests": 1024, "latency_ms": 42, "error_rate": 0.003}
}

# Styling With A Class Name

Pass `className` to attach an arbitrary class to the parent node znai creates.
Use it to decorate the wrapper without touching the function:

```markdown
:include-javascript-function: themeBox {
    label: "highlighted by an outer border",
    className: "theme-box-with-border"
}
```

:include-file: javascript/theme-box.css {title: "theme-box.css", readMore: true}

:include-javascript-function: themeBox {
    label: "highlighted by an outer border",
    className: "theme-box-with-border"
}

The dashed outline comes from the class on the outer wrapper — the box itself
is untouched.

# Search

Values passed via `args` flow into znai's local search. Searching for any word
inside the parameters surfaces the page and the rendered block.

# Sample Function

The function below powers the examples on this page. It reads the initial
theme and subscribes to future changes.

:include-file: javascript/theme-box.js {title: "plugins/javascript/theme-box.js"}
