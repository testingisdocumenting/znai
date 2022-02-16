# Markdown

The fastest way to learn Markdown is to go to [CommonMark](https://commonmark.org/help/) website
and go through a 60 second cheatsheet or 10 minute tutorial.

# Running Znai

Znai is available as

* [Command Line](#command-line)
* [Maven Plugin](#maven-plugin)

Both command line and maven plugin lets you to build documentation website and also run preview server during 
documentation writing process.

# Command Line

:include-markdown: {firstAvailable: ["getting-started-installation-override.md", "getting-started-installation.md"]}

# Maven Plugin

:include-file: maven-plugin.xml {title: "maven plugin"}

Note: consider adding a separate module for documentation in the multi module maven project

# Scaffolding

To create a minimum set of files for your documentation execute 

```tabs
CLI: :include-cli-command: znai --new
Maven: :include-cli-command: mvn znai:new
```

    znai    
       |--chapter-one
                 |--page-one.md
                 |--page-two.md
       |--chapter-two
                 |--page-three.md
                 |--page-four.md
       |--toc
       |--lookup-paths
       |--meta.json

Listed above directories and files will be generated in the current directory. 

# Preview Mode

Znai comes with `preview` mode. In `preview` mode, znai monitors changes you apply to the documentation files and notifies browser
to navigates to the right page and highlights the changes.

To start preview mode, navigate to the documentation directory (`znai` if you used scaffold) and run

```tabs
CLI: :include-cli-command: znai --preview [--port port-number]
Maven: 

:include-cli-command: mvn znai:preview

:include-file: maven-plugin-cfg.xml {title: "preview port", excludeRegexp: ["docId"], highlight: "port"}
```

Open URL output by the command and open in a browser.
Blue *eye* icon in the top right corner indicates that preview is on.

Open any text editor, modify and save a markdown file.
Changes will be reflected in the browser. 

# Static Site Generation 

To build static documentation site you need to provide `documentation id`. 
Documentation id becomes part of your url and is also used inside generated HTMLs to reference static resources 
using absolute path.  

For example, znai original documentation is hosted on [https://testingisdocumenting.org/znai](https://testingisdocumenting.org/znai)
and documentation id is `znai`.

This original page is hosted on [https://testingisdocumenting.org/znai/introduction/getting-started](https://testingisdocumenting.org/znai/introduction/getting-started).
znai generates each page as index.html and puts inside directories to make it possible to have an extension less urls.

To make it easier to handle static resources loading, Znai builds all the urls inside HTML pages using absolute locations,
and that's why it is required to provide `documentation id` during the site generation.

To generate static site, use

```tabs
CLI: :include-cli-command: znai --doc-id my-docs --deploy /path/to/static-content {highlight: "doc-id"}
Maven: :include-file: maven-deploy.xml {title: "maven plugin", highlight: "docId"} 
```

# GitHub Pages

To deploy to [GitHub Pages](https://pages.github.com) use [GitHub Pages Action](https://github.com/peaceiris/actions-gh-pages).

Here is an example of znai publishing its documentation to github pages

:include-file: workflows/documentation-deploy.yaml {
  title: "github action",
  startLine: "name: deploy documentation",
  endLine: "publish_dir"}

# On-site Central Hub Deployment

Znai has enterprise mode that lets you run Documentation hub inside your organization.
It is completely free and open sourced.

Please create a GitHub issue if you want to try it out. I don't document it yet as I only have a couple of scenarios 
I tried it on, and I need more input.