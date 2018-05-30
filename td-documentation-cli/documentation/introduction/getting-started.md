# Command Line Tool

Mdoc comes with a command line tool that:
* Generates a documentation website
* Runs auto-preview on a local server
* Runs documentation server


# Markdown

Fastest way to learn Markdown is to go to [CommonMark](http://commonmark.org/help/) website 
and go through a 60 seconds cheatsheet or 10 minutes tutorial.

# Installation

[Optin](https://cmdb.twosigma.com/entities/mdoc-opt-in) to add the command line to your hosts.
Check tool presence by running 

:include-cli-command: mdoc --help

# Scaffolding

To create a minimum set of files for your documentation execute 

:include-cli-command: mdoc --new

    mdoc
       |--chapter-one
                 |--page-one.md
                 |--page-two.md
       |--chapter-two
                 |--page-three.md
                 |--page-four.md
       |--toc
       |--lookup-paths
       |--meta.json
       
# Preview 

Navigate to the newly created directory. Start preview mode by running

:include-cli-command: mdoc --preview 

The command will output a URL that directs to your preview.  
  
Blue *eye* icon in the top right corner indicates that preview is on.
Open any text editor, modify and save `page-one.md` file.
Changes will be reflected in the browser. 

# Deploy

1. Build static files

    :include-cli-command: mdoc --doc-id <doc-id> --source <mdoc-dir> --deploy <dest-dir> {paramsToHighlight: "dest-dir"}
    
2. Make static files globally available

    :include-cli-command: mdoc-upload --mdoc-root-path <dest-dir> {paramsToHighlight: "dest-dir"}


Your documentation will be available shortly at `http://mdoc.app.twosigma.com/<doc-id>`

Warning: Always check that a new doc-id is not taken before deploying documentation. There is no ownership check at the moment
and one can override existing documentation. 
 