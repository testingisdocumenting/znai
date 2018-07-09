MDoc currently supports two deployment workflows: [direct deployment](deployment/workflows#direct-deployment) and [auto-deployment from the monorepo](deployment/workflows#auto-deployment-from-the-monorepo).

# Direct Deployment

## Upload Documentation

To build and publish MDoc documentation, which is hosted by [TS Guides](deployment/TS-guides), run this command in your documentation directory:

:include-cli-command: mdoc --upload --doc-id <your-doc-id> {paramsToHighlight: "<your-doc-id>"}

Warning: Before initial deploy, verify your `doc-id` is not in use by another user by  navigating to the TS Guides URL for your `doc-id` string (https://tsguides.app.twosigma.com/`doc-id`).

The command will output a URL for your documentation, where you can verify the upload was successful.

## Register Documentation in CMDB
 Follow the steps under [Registration](deployment/registration).

# Auto-Deployment from the Monorepo

If your documentation lives in the monorepo (i.e., VATS), you can have your documentation auto-deploy on push. To make use of this workflow, do the following:

## Create Documentation Codebase

Create a new codebase for your documentatiomn and, in its `software.mi`, add dependencies to `ts_testing_documenting` and any other codebases that you might reference in your docs.

## Configure Makefile

Modify your new codebase's top-level Makefile to include MDoc references and define a doc-id:

Warning: Before initial deploy, verify your `doc-id` is not in use by another user by  navigating to the TS Guides URL for your `doc-id` string (https://tsguides.app.twosigma.com/`doc-id`).

:include-file: deployment/makefile-no-lookup.make {readMore: true, readMoreVisibleLines: 5}

If your documentation will pull in files and code snippets from other codebases in the monorepo, also define and add the codebases to your documentation's lookup-path file in the Makefile:
:include-file: deployment/makefile-with-lookup.make {readMore: true, readMoreVisibleLines: 5}

Note: In addition to allowing you to pull in artifacts from other codebases, referencing these dependencies in your Makefile validates the accuracy of your docs against these codebases at build time. \
\
If you want these benefits without auto-deploying on push, simply remove `--deploy dist/mdoc` from `${TS_MDOC_HOME}/bin/mdoc --doc-id <add-your-doc-id-here> --source mdoc --deploy dist/mdoc` in your Makefile.

## Add Codebase to CMDB Registry

Complete the steps under [Registration](deployment/registration), being sure to add your codebase name (e.g., `ts_my_codebase_name`) to the optional field `ts_documentation_vats_codebase_name`.

