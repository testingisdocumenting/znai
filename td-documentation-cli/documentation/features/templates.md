# Don't repeat yourself

Define templates when you need to repeat the same layout more than once.

For example

**Name**: John

**Title**: Software Engineer

**Current Project**: BBR
 
**Team**: Team A
 
**Code Example**: 
```java
class BestClass {}
```
\
\
Second copy & paste
\
\
**Name**: Sathish

**Title**: Software Engineer

**Current Project**: BBR
 
**Team**: Team A 

**Code Example**: 
```java
class AwesomeClass {}
```

# Definition

Instead define a template in a separate file
 
:include-file: templates/job.md

Insert it using fenced block syntax. Content inside of fenced block is treated as key/values. 

    ```template templates/job.md
    name: Sathish
    title: Software Engineer
    team: Team A
    project: BBR
    code:
    `````java
    class AwesomeClass {}
    `````    
    ```
    
Result is rendered below
    
```template templates/job.md
name: Sathish
title: Software Engineer
team: Team A
project: BBR
code:
`````java
class AwesomeClass {}
`````    
```