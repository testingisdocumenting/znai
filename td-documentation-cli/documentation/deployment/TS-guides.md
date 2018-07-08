# Hosting Solution

MDoc documentation is deployed via TS Guides, which offers a centralized hosting solution for user documentation.

In addition to providing hosting, TS Guides offers:
* [Centralized landing page](https://tsguides.app.twosigma.com) where authors can optionally display their documentation by category with a short description.
* Automatic indexing for Two Sigma's [federated search](https://search.app.twosigma.com) under the User Guides collection.

# Sphinx Documentation

TS Guides also supports documentation created with Sphinx.

Sphinx documentation can be deployed in two ways, similar to the [MDoc deployment options](deployment/workflows), either by direct deployment or automatically from the monorepo.
## Direct Deployment

Deploy Sphinx docs to TS Guides directly using the `tsguides` CLI tool:

**1. Select doc-id for documentation**

Choose a simple and human-readable string using URL-safe characters for your `doc-id`, and verify it is not already in use by navigating to the TS Guides URL for your `doc-id` string (i.e., https://tsguides.app.twosigma.com/`doc-id`).

**2.  Upload documentation files**

Run the following command:

:include-cli-command: tsguides --upload --doc-id <your-doc-id> --path <path/to/built/html> {paramsToHighlight: ["your-doc-id", "ath/to/built/html"]}

The `tsguides` command will output a URL. Navigate to this URL and verify that your documentation was successfully uploaded.

**3.  Register documentation**

Follow the steps for [registering your documentation](deployment/registration) in CMDB.


## Auto-Deployment from Monorepo

If your Sphinx docs are in the monorepo (i.e., VATS) you can have them automatically update when a change is pushed. 

**1. Select doc-id for documentation**

Choose a simple and human-readable string using URL-safe characters for your `doc-id`, and verify it is not already in use by navigating to the TS Guides URL for your `doc-id` string (i.e., https://tsguides.app.twosigma.com/`doc-id`).

**3.  Register documentation**

Using this `doc-id`, follow the steps for [registering your documentation in CMDB](deployment/registration), being sure to add your codebase name (e.g., `ts_my_codebase_name`) to the optional field `ts_documentation_vats_codebase_name`.