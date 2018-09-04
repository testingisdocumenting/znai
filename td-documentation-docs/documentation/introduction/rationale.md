# Stale Documentation

Keeping documentation up to date is a hard task. 
The further documentation is from the code the more likely it won't be updated when a feature is added or changed.

Depending on your product and clients, stale documentation effects are:
* `icon:time` Users' time lost
* `icon:thumbs-down` Reputation damage

# Part Of Workflow

As part of your code review today, you check that:
* No design violations are committed
* No subtle bugs are introduced
* All tests are updated 

Now, it is time to add to your list: 
* Documentation is updated
 
# Maintenance 

Keeping documentation up to date takes time. 
Even non-functional changes may require documentation update.
Here are some activities that most likely put your documentation out of sync:
* Moving UI elements around
* Re-naming REST response fields 
* Removing redundant command line parameters
* Renaming public API classes
 
Our code base already contains a vast amount of data. 
Instead of requiring that you copy-and-paste, we leverage already-existing artifacts.
* Examples of how to use API (part of code) 
* Config files
* Test results
    * Web UI
    * REST
    * CLI
    * Business logic

# Familiar Approach

Markup is everywhere
* StackOverflow
* GitHub
* Jupyter
* Reddit
* Discourse 

Markup-based documentation is widely used as well
* Open source projects (ReactJS, Pandas, Spark)
* Technical books (O'Reilly, Manning)
* Big companies (Google, FaceBook)

# Presentations

Mdoc builds presentations to: 
* Show new features
* Teach a class from your documentation

Like all documentation, maintaining presentations takes effort. 
Instead of building separate slides and trying to keep them up to date, 
this system automatically generates slides from your documentation content.