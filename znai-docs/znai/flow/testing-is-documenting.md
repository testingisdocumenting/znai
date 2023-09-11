# Auto Maintainable Content

There is a lot of content you can get by leveraging automated testing. And most importantly that content is going to be auto maintained.

Let's say you document a command line tool, and you want to provide an example of some operation output.
You can copy and paste the output risking it being outdated. Or, you can write a test that exercises a command line and also captures its output.

You end up getting a lot of benefits:
* your app is tested
* your documentation is auto updated

# Examples

In addition to command line tools, here are some common scenarios you should consider:

* Test Web UI - *application screenshots*
* Test Business Logic - *tabular data*
* Test REST API - *API JSON real examples*
* Config files
* Script snippets
* Runtime dependencies DAGs
* ...

# Self Powering Cycle

As you write your documentation you will come up with new scenarios to test. As you write tests, think about your documentation and what test-produced artifacts can improve it.

Znai provides a lot of utilities to include test artifacts.

In [Synergy With Testing](synergy-with-testing/web-UI) chapter you will see examples of UI, REST API and Business Logic.    