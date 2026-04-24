/*
 * custom javascript global function to render custom content within znai
 * generated guides
 *
 * renders a panel that lists every argument passed via the plugin as
 * a key/value row. reacts to znai theme switches via the theme observable
 * to toggle a `theme-dark` class that drives the accent color via CSS.
 */
(function () {
  function formatValue(value) {
    if (value === null || value === undefined) {
      return String(value);
    }

    if (typeof value === "object") {
      return JSON.stringify(value);
    }

    return String(value);
  }

  function createElement(tagName, className, textContent) {
    var el = document.createElement(tagName);
    el.className = className;
    if (textContent !== undefined) {
      el.textContent = textContent;
    }
    return el;
  }

  window.themeBox = function (node, args, themeObservable) {
    var box = createElement("div", "theme-box");
    var titleEl = createElement(
      "div",
      "theme-box-title",
      "args passed to the function"
    );
    var list = createElement("div", "theme-box-list");
    var themeEl = createElement("div", "theme-box-current-theme");

    var argKeys = Object.keys(args);
    if (argKeys.length === 0) {
      list.appendChild(
        createElement("div", "theme-box-empty", "no args passed")
      );
    } else {
      argKeys.forEach(function (key) {
        list.appendChild(createElement("div", "theme-box-key", key));
        list.appendChild(
          createElement("div", "theme-box-value", formatValue(args[key]))
        );
      });
    }

    box.appendChild(titleEl);
    box.appendChild(list);
    box.appendChild(themeEl);
    node.appendChild(box);

    function applyTheme(themeName) {
      box.classList.toggle("theme-dark", themeName === "dark");
      themeEl.textContent = "current theme: " + themeName;
    }

    applyTheme(themeObservable.current);
    var unsubscribeFromTheme = themeObservable.subscribe(applyTheme);

    return function cleanup() {
      unsubscribeFromTheme();
      console.log("themeBox cleanup: removed theme subscription");
    };
  };
})();
