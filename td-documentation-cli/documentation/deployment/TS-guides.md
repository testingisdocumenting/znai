# Hosting Solution

MDoc documentation is deployed via TS Guides, which offers a generator-agnostic centralized hosting solution for user documentation.

In addition to providing hosting, TS Guides offers:
* [Centralized landing page](https://tsguides.app.twosigma.com) where authors can optionally display their documentation by category with a short description.
* Automatic indexing for Two Sigma's [federated search](https://search.app.twosigma.com) under the User Guides collection.

# Non-MDoc Documentation

TS Guides supports non-MDoc generated HTML documentation, e.g. Sphinx.

This documentation can be deployed with the `tsguides` CLI tool, using the workflow below:

## 1.   Select doc-id for documentation 

Choose a simple and human-readable string using URL-safe characters for your `doc-id`, and verify it is not already in use by navigating to the TS Guides URL for your `doc-id` string (i.e., https://tsguides.app.twosigma.com/`doc-id`).

## 2.  Upload documentation files

In the directory containing your built HTML documentation files, run the following command:

:include-cli-command: tsguides --upload --doc-id <your-doc-id> --path <path/to/your/docs> {paramsToHighlight: ["your-doc-id", "path/to/your/docs"]}

The `tsguides` command will output a URL. Navigate to this URL and verify that your documentation was successfully uploaded.

## 3.  Register documentation

Follow the steps for [registering your documentation in CMDB](deployment/workflows#register-documentation)
