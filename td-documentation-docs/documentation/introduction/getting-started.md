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

See [documentation under Deployment](deployment/workflows)

# Maven

MDoc includes a Maven plugin which allows both preview of documentation and generation.  Example plugin configuration:
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.twosigma.td</groupId>
                <artifactId>td-documentation-maven-plugin</artifactId>
                <version>0.1</version>
                <executions>
                    <execution>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <docId>mdoc</docId>
                    <deployRoot>${project.build.directory}</deployRoot>
                    <sourceRoot>${project.basedir}/documentation</sourceRoot>
                    <port>3334</port>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

`docId` and `deployRoot` are only required for building documents while `port` is only applicable to preview and defaults 
to 3333.  `sourceRoot` is the directory where `toc` exists.

With the configuration above, documentation will be built as part of the `package` goal and will end up in this module's
target directory, in a subdirectory named after the value of `docId`.

In order to preview documentation, run `com.twosigma.td:td-documentation-maven-plugin:0.1:preview` in the appropriate module.
