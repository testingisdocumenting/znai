# description

The vats maven-import command allows one to automatically create an unlimited number of external codebases, each of which contains containing one jar, given a maven jar.

For example, if we wanted to import commons-lang3 from mvnrepository.com, we would vats maven-import using the SBT dependency line

vats maven-import --sbt '"org.apache.commons" % "commons-lang3" % "3.4"'

The command stores

    the source jar in a folder <codebase_root>/java/non-deployable under the original file name ending with -sources.jar
    the POM file as <codebase_root>/pom.xml
    the library itself under  <codebase_root>/java/lib

If you want to use an external java library, find its Maven groupid, artifactid and version on the web site of the open source organization creating the library or on mvnrepository.com or search.maven.org.

Once you have this information called GAV coordinates (for Group/Artifact/Version), you can run vats maven-import.

vats maven-import is hard-wired to refuse any software with the Gnu Public License. 

vats maven-import only imports the GAV's which are not already present in VATS.

The software.mi files of the generated codebases contain the proper dependency information in the STANDARD_JARS key, and proper MAVEN_GROUPID, MAVEN_ARTIFACTID, and MAVEN_VERSION keys. See Software.mi Makepath And Friends for details.

```` 
usage: vats.py maven-import [-h] [--vats-verbose] [--vats-profile]
                            [--vats-properties PROPERTIES_FILE]
                            [--vats-property VATS_PROPERTY]
                            [--vats-ignore-sanitycheck] [--vats-debug]
                            [--vats-json-debug] [--vats-root ROOT]
                            [--vats-targetarch VATS_TARGETARCH]
                            [--vats-only-with CODEBASE]
                            [--vats-config VATS_CONFIG] [--sbt SBT_DEPENDENCY]
                            [-g MAVEN_GROUP_ID] [-a MAVEN_ARTIFACT_ID]
                            [-v MAVEN_VERSION] [-p MAVEN_PROFILE]
 
Import using:
    maven-style import: -g <GROUP> -a <ARTIFACT> -v <VERSION>
    sbt-style import  : --sbt '"<GROUP>" % "<ARTIFACT>" % "<VERSION>"'
 
optional arguments:
  -h, --help            show this help message and exit
  --sbt SBT_DEPENDENCY  Sbt-style dependency, example below (default: None)
  -g MAVEN_GROUP_ID, --maven-group-id MAVEN_GROUP_ID
                        Maven group id. (default: None)
  -a MAVEN_ARTIFACT_ID, --maven-artifact-id MAVEN_ARTIFACT_ID
                        Maven artifact id. (default: None)
  -v MAVEN_VERSION, --maven-version MAVEN_VERSION
                        Maven version. (default: None)
  -p MAVEN_PROFILE, --maven-profile MAVEN_PROFILE
                        Maven profile to activate. This is an optional
                        argument which can be repeated. A Maven profile is a
                        configuration defined in a POM file which can be
                        activated or not. (default: [])
 
Example: vats maven-import --sbt '"org.apache.commons" % "commons-lang3" % "3.4"'
 
Read more: http://wiki/confluence/display/tools/vats+maven-import
To display global options, re-run with --vats-verbose --help
````

# codebase naming

> The names of the new codebases created by this command will be built in the following way :

 

``ext_public_orgid_artifactid_version`` 

 

> By taking the original orgId, artifactId and version from the maven repository 
> and replacing the characters which are not allowed in VATS by z.

 



We realize this naming convention is not ideal. 
VATS codebase names are used to generate identifiers in bash, 
so allowing dashes and periods in codebase names would have required changes in many scripts 
across the Two Sigma codebase.. We consulted with several subject matter experts and concluded that replacing dashes and periods with a z was the most reasonable option.

# Usage Scenario

- The user determines that a new open source library is needed and finds its coordinates,
 either on the web site of the library or on [mv*nreposi*tory.com](http://mvnrepository.com) or ...

- User executes a command
  - Using sbt-style import:  ``vats maven-import --sbt '"groupId" % "artfifactId" % "version"'``
  - Using maven-style import:  ``vats maven-import -g groupId -a artfifactId -v version``

- VATS finds (jars to import) = (all maven dependency jars) - (maven jars already in VATS).

- VATS finds all (jars to imports) in the internal mirror.

- VATS creates new external codebases for each dependency jar.

- The user runs another VATS command for code review concerning these new external codebases.

- Once the review is approved, the user pushes the new codebases along with other changes possibly.

