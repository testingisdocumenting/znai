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

If you want your users be happy you have to keep documentation in sync with you product. It takes time.
Not every change to the product is a new functionality. But it may still require a documentation update. 
Here are some activities that most likely put your documentation out of sync:
* moving UI elements around
* renaming REST response fields 
* removing redundant command line parameters
* renaming public API classes

This does not mean that we always have to copy-and-paste code snippets, REST responses and retaking screenshots. 
Our code base already contains a wast amount of data. And some extra data can be created by running tests.

Here are some of the things that we can use to make a documentation automatically up-to-date:
* examples of how to use API (part of code) 
* config files
* test results
    * Web UI
    * REST
    * CLI
    * business logic

Doing copy-paste between sources takes time and is getting abandoned quickly.
What we can do instead is refer artifacts that are around code base. 
