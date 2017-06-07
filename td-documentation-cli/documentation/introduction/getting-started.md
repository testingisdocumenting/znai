# Command Line Tool

Mdoc comes with a command line tool that:
* generates documentation website
* runs auto-preview local server
* runs documentation server


# Markdown

Fastest way to learn markdown is to go to [CommonMark](http://commonmark.org/help/) website 
and go through a 60 seconds cheatsheet or 10 minutes tutorial.

# Installation

[Optin](https://optin) to add the command line to your hosts.
In your terminal execute `mdoc --help` to make sure tool is available.

# Scaffolding

Execute `mdoc --new` to create a minimum set of files for your documentation.

    mdoc
       |--chapter1
                 |--page1.md
       |--chapter2
                 |--page2.md
       |--toc
       |--meta.json
       
# Preview 

Navigate to the newly created directory. Execute `mdoc --preview`. 
Open prompted url inside your browser.
  
Blue *eye* icon in the top right corner indicates that preview is on.
Open any text editor, modify and save `page1.md` file.
Changes will be displayed in the browser. 