/*
 * sample plugin showing a list of rows that grows to fit its content.
 *
 * the parent node sized by znai (`height` arg) is what constrains us — when
 * unset, the feed renders all rows tall enough to show them all; when set,
 * the same content scrolls inside the fixed viewport znai gave us.
 */
(function () {
  function createElement(tagName, className, textContent) {
    var el = document.createElement(tagName);
    el.className = className;
    if (textContent !== undefined) {
      el.textContent = textContent;
    }
    return el;
  }

  function buildRow(event) {
    var row = createElement("div", "activity-feed-row");
    row.appendChild(createElement("span", "activity-feed-time", event.time || ""));
    row.appendChild(createElement("span", "activity-feed-action", event.action || ""));
    row.appendChild(createElement("span", "activity-feed-detail", event.detail || ""));
    return row;
  }

  window.activityFeed = function (node, args, themeObservable) {
    var events = Array.isArray(args.events) ? args.events : [];

    var feed = createElement("div", "activity-feed");
    events.forEach(function (event) {
      feed.appendChild(buildRow(event));
    });

    node.appendChild(feed);

    function applyTheme(themeName) {
      feed.classList.toggle("activity-feed-dark", themeName === "dark");
    }

    applyTheme(themeObservable.current);
    themeObservable.subscribe(applyTheme);
  };
})();
