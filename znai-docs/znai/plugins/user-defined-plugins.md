# Overview

User-defined plugins let you register `:include-<id>:` and fenced block plugins
straight from documentation — no Java code, no rebuild of znai. Each plugin is
a JSON file next to your docs that declares:

* a unique `id` to reference from markdown
* a `type` — either `include` or `fence`
* a FreeMarker `template` rendered on every use
* `arguments` — types, optional allowed values, and a `required` flag

znai renders the template with the parameters you passed, parses the output as
markdown, and folds the result back into the page — same machinery as
[include-template](layout/templates), with argument validation on top. A typo or
missing required argument fails the build instead of silently emitting nothing.

# Registering A Plugin

List each plugin config under `plugins` in `extensions.json`. Paths resolve
relative to your documentation root, like `cssResources` and `jsResources`.

```json {title: "extensions.json"}
{
  "plugins": ["plugins/themed-box-plugin.json"]
}
```

# Plugin Config Shape

:include-file: plugins/themed-box-plugin.json {title: "plugins/themed-box-plugin.json"}

`id` is what you type after `:include-` or as the fence language. A config
registers exactly one role; if you need both an include and a fence variant of
the same template, write two configs that point at the same `.ftl` — they take
different special arguments (`freeForm` vs `fenceContent`) anyway.

# Arguments

Each entry under `arguments` defines one parameter callers can pass.

```json
{
  "type": "string",
  "required": true,
  "available": ["info", "success", "warning"]
}
```

* `type` — `number`, `string`, `list-of-number`, `list-of-string`, or `list`
* `required` — defaults to `false`
* `available` — optional; an inline array of allowed values, or a
  `$path/to/file.json` reference to a JSON array file

Unknown arguments, type mismatches, and values outside `available` all fail the
build with the same error reporting as built-in plugins.

## Available Values From A File

Point `available` at a JSON array when the allowed set is owned elsewhere — a
team-wide tag list, regional names, severity levels shared with another tool:

:include-file: plugins/themed-box-tags.json {title: "plugins/themed-box-tags.json"}

The file is loaded through znai's resource resolver, so it works next to `toc`,
on the lookup path, or packaged in a jar.

# Special Arguments

Two argument names are reserved:

* `freeForm` — the text right after the plugin id and before `{...}`. Use it
  for the single primary input (a message, an entity name, a URL). No `type`
  needed — it's always raw text. Set `required: true` to fail the build when
  the caller omits it; defaults to optional.
* `fenceContent` — the body of a fenced block; only meaningful for fence
  plugins. No `type` needed — the raw text is passed through untouched. Always
  present by construction.

```json
"freeForm": { "required": true }
```

Both names skip the regular type/`available` validation.

# Template

Templates are rendered with [FreeMarker](https://freemarker.apache.org), same
as [include-template](layout/templates). Every argument — including `freeForm`
and `fenceContent` — is a top-level variable. The output can be any markdown
znai understands: prose, fenced code, calls to other plugins.

:include-file: plugins/themed-box-plugin.ftl {title: "plugins/themed-box-plugin.ftl"}

This example wraps [include-javascript-function](plugins/javascript-plugin)
and forwards `severity` and `tags`, shrinking the surface area writers need to
remember.

# Usage

```markdown
:include-themed-box: Deploy finished in 4 minutes flat {
  severity: "success",
  tags: ["deployment", "latency"]
}
```

:include-themed-box: Deploy finished in 4 minutes flat {
  severity: "success",
  tags: ["deployment", "latency"]
}

```markdown
:include-themed-box: Capacity approaching threshold {
  severity: "warning",
  tags: ["observability"]
}
```

:include-themed-box: Capacity approaching threshold {
  severity: "warning",
  tags: ["observability"]
}

Omit a required argument or pass a tag missing from `themed-box-tags.json` and
the build stops with a descriptive error.

# Fence Plugins

Set `"type": "fence"` to register a fenced block plugin. The body comes through
the special `fenceContent` argument:

:include-file: plugins/custom-fence-block-plugin.json {title: "plugins/custom-fence-block-plugin.json"}

:include-file: plugins/custom-fence-block-plugin.ftl {title: "plugins/custom-fence-block-plugin.ftl"}

FreeMarker's `?json_string` escapes the body so it stays a valid JSON string
when handed to another plugin's params block — multi-line bodies, quotes, and
backslashes survive intact.

The fence marker is the plugin id, not a language hint:

    ```custom-fence-block {title: "greet.js"}
    function greet(name) {
      console.log("hello, " + name);
    }

    greet("world");
    ```

```custom-fence-block {title: "greet.js"}
function greet(name) {
  console.log("hello, " + name);
}

greet("world");
```

The body reaches `include-javascript-function` verbatim as the `snippet` arg.
Any fence plugin can use this pattern to forward content to diagram renderers,
code annotators, or other include plugins.

# Rebuild Detection

The plugin config, the template, and any referenced `available` files are
treated as auxiliary files. Edit any of them and znai regenerates the affected
pages — same contract as template includes and snippet files.

# Search

Values you pass to the plugin flow into znai's local search via the inner
plugin's `textForSearch`. No extra work to make pages searchable by parameter
content.

# When To Use

User-defined plugins fit when:

* you keep pasting the same include block with only a few values changed
* writers should pick from a constrained set (an enum-like `severity`, a tag
  registry)
* a snippet spans multiple plugins and you want one entry point that composes
  them consistently

If the reuse is purely structural and needs no validation,
[include-template](layout/templates) is enough on its own.
