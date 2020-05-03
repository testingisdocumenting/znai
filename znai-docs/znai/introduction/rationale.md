# Stale Documentation

Keeping documentation up to date is a hard task. 
It's often the case that the more separated your documentation is from the code it's describing, the more likely it won't be updated when a feature is added or changed.

Depending on your product and clients, stale documentation can cause:
* `:icon:time` Lost time for users
* `:icon:thumbs-down` Reputational damage

# Part Of Workflow

As part of your code review today, you likely check that:
* No design violations are committed
* No subtle bugs are introduced
* All tests are updated 

Now, it is time to add to that list: 
* Documentation is updated
 
# Maintenance 

Keeping documentation up to date takes time. 
Even non-functional changes may require documentation updates.
Here are some activities that most likely put your documentation out of sync:
* Moving UI elements around
* Re-naming REST response fields
* Removing redundant command line parameters
* Renaming public API classes
 
Your codebase probably already contains a vast amount of data. 
Instead of using copy-and-paste to duplicate that data in your documentation, you should leverage the already-existing artifacts.
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

`Znai` builds presentations to: 
* Show new features
* Teach a class from your documentation

Like all documentation, maintaining presentations takes effort. 
Instead of building separate slides and trying to keep them up to date, 
this system automatically generates slides from your documentation content.