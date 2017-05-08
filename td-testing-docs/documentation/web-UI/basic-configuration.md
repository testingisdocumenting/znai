# Base URL

Robust tests don't specify full URL of an application under test.
Instead you pass only relative URL to functions like `open`.

:include-groovy: examples/Basic.groovy

Define base URL portion either inside a `test.cfg` file

:include-file: examples/test.cfg {lang: "groovy"}

or pass as a command line argument `--url=http://...`