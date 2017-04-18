# Stale Documentation

Keeping documentation up-to-date is a hard task. 
The further documentation is from the code the more likely it won't be updated when a feature is added or changed.

Depending on your product and clients, stale documentation effects are:
* users' time lost
* reputation damage

# Part Of Workflow

As part of your code review you check that there are:
* no design violations
* no subtle bugs
* tests are updated 

Now it is time to add to it
* documentation is updated
 
# Maintenance 

Keeping documentation up-to-date takes time. 
Even non functional changes may require documentation update.
Here are some activities that most likely put your documentation out of sync:
* moving UI elements around
* renaming REST response fields 
* removing redundant command line parameters
* renaming public API classes
 
Our code base already contains a wast amount of data. 
Instead of copy-and-paste we leverage artifacts around us.
* examples of how to use API (part of code) 
* config files
* test results
    * Web UI
    * REST
    * CLI
    * business logic

# Familiar Approach

Markup is everywhere
* StackOverflow
* GitHub
* Jupyter
* Reddit
* Discourse 

Markup based documentation is widely used as well
* open source projects (ReactJS, Pandas, Spark)
* technical books (O'Reilly, Manning)
* big companies (Google, FaceBook)

# Presentations

We build presentations to 
* show new features
* teach a class

There is a cost to maintain them. 
Instead of building separate slides and keep them up-to-date, 
this system automatically generates slides from your documentation content.