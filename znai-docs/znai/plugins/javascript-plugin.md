# Overview

Use `include-javascript-function` to delegate rendering of a block to a plain JavaScript function.
Znai will create a DOM node for your function and hand it over together with:

* the DOM node to render into
* the parameters you passed from markdown (as `args`)
* a `themeObservable` — so your rendering can react to light/dark theme switches

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

See [themeBox](#sample-function) definition at the end of this page.

# Registering A Function

Attach your function to `window` under the name you want to reference from markdown.
Load the JavaScript file through [`extensions.json`](configuration/extensions):

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

* `node` — a `div` parent div to append content you need.
* `args` — an object with the parameters passed from markdown.
  Framework-level parameters (`title`, `wide`, `className`, `anchorId`) are handled by znai and are **not** forwarded here.
* `themeObservable` — live access to the current znai theme.

```javascript {title: "themeObservable shape"}
{
    current: "light" | "dark",
    subscribe(listener) {
        // listener is called with the new theme name whenever the theme changes
        // returns an unsubscribe function
        // good place to unsubscribe from theme change notifications
    }
}
```

Your function may return a cleanup function. Znai calls it when the block is removed
or re-rendered — a good place to dispose of timers, external event listeners,
or any other resources you allocated.

The [sample function](#sample-function) below returns a cleanup that logs to the
browser console:

:include-file: javascript/theme-box.js {
  title: "theme-box.js",
  surroundedByScope: {start: "function cleanup", scope: "{}"}
}

To see it fire, open your browser DevTools console on this page, then navigate to
any other page — znai unmounts the block and your cleanup runs. A single page
visit triggers multiple cleanups because every `themeBox` rendered above is
torn down.

# Theme Toggle

:include-javascript-function: themeBox {
    message: "Switch the theme to see me update.",
    tags: ["demo", "theme"]
}

Toggle the theme using the switcher in the bottom left corner and observe the custom content change style.
This can be done via 
```javascript
themeObservable.subscribe(applyTheme);
````

# Title

Pass `title` to render a title bar above the block. Znai handles the title
through its shared container, so your function never sees it.

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

Pass `wide: true` to let the block span the full width of the page, matching
the behavior of wide images and iframes.

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
Use it to add decorations around the rendered block without touching the
function itself:

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

The dashed outline comes from the class attached to the outermost wrapper — the
box itself is untouched.

# Search

Values passed via `args` participate in znai's local search.
Searching for any word used inside the parameters will surface the page and the rendered block.

# Sample Function

The following function powers the example above. It demonstrates reading the
initial theme and subscribing to future changes.

:include-file: javascript/theme-box.js {title: "plugins/javascript/theme-box.js"}
