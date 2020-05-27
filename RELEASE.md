# Setup

## Sonatype/Maven Central

There is some one time setup required in order to publish to Maven Central.  First, you will need a Sonatype account which
you can create at https://issues.sonatype.org/secure/Signup!default.jspa.  You will also need permissions to publish to
the `org.testingisdocumenting.znai` group ID, for that raise a similar JIRA to https://issues.sonatype.org/browse/OSSRH-41183.

Once you have an account, you will need to add credentials for Sonatype to your `~/.m2/settings.xml`.  If you don't have
one then copy the one below.  If you have one already then create or modify the `servers` section to include the server
as shown below:

```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>ossrh</id>
            <username>name</username>
            <password>password</password>
        </server>
    </servers>
</settings>
```

## GPG

You will need to also download GPG files to be copied into your `~/.gnupg` and the corresponding passphrase from 1password.
```
$ brew install gnupg gnupg2
$ cd ~/
$ mkdir ~/.gnupg
$ chmod 700 .gnupg
$ cd .gnupg
$ mv ~/Downloads/public.key .
$ mv ~/Downloads/private.asc .
$ gpg --import public.key
$ gpg --import private.asc # you need to enter the passphrase from 1password
```

## Java
You need to set PATH to execute maven and JAVA_HOME to run maven and java.
```
$ cat ~/.profile 
# location of mvn. You might need to download maven from https://maven.apache.org/download.cgi if you don't have it.
export PATH=$PATH:~/local/maven/bin 
export JAVA_HOME=$(/usr/libexec/java_home)
```


# Prepare release

The first step is to prepare the release.  Make sure you are on master and up to date then run:

```
mvn release:clean release:prepare 
```

This will do a number of things (let's assume you're trying to release version x.y.z):
* build znai
* prompt you for a few version related things where you should generally accept the proposed values
* update the version number in all poms to x.y.z
* git commit the change
* tag git as x.y.z
* update versions to x.y.(z+1)-SNAPSHOT
* git commit the change

# Perform the release

```
export GPG_TTY=$(tty)
mvn release:perform 
```

This will prompt you for the GPG passphrase.  It will then build and test znai and publish all artifacts to Maven Central.

# Sit back, relax and enjoy the wait

At this stage, **be patient**.  There is some delay completion of the `release:perform` step and the artifacts being
available in Maven Central.  This is of the order of hours.  You can keep refreshing https://search.maven.org/search?q=g:org.testingisdocumenting.znai%20AND%20a:znai&core=gav
until you see your version.
