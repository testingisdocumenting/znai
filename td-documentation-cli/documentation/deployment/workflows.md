# Deploy MDoc

To build and publish MDoc documentation, which is hosted by [TS Guides](deployment/TS-guides), run this command in your documentation directory:

:include-cli-command: mdoc --upload --doc-id <your-doc-id> {paramsToHighlight: "<your-doc-id>"}

Warning: Before initial deploy, verify your `doc-id` is not in use by another user by  navigating to the TS Guides URL for your `doc-id` string (https://tsguides.app.twosigma.com/`doc-id`).

The command will output a URL for your documentation, where you can verify the upload was successful.

# Register Documentation

Warning: Documentation must be registered in CMDB if it is to persist in the database, get indexed by search, or be displayed on the TS Guides landing page. Only documentation meant to be ephemeral (e.g., for purposes of a code review) should be left unregistered.

To register, create a new [ts_documentation entity in CMDB](https://cmdb.twosigma.com/entity_types/ts_documentation)
1.  Provide a unique doc-id in the first field, "Ts documentation"
2.  Choose a documentation type (mdoc or sphinx) for "TS documentation type"
3.  Provide easy-to-read display title for "TS documentation title"
4.  Provide content category for "TS documentation category" (see [TS Guide landing page](https://tsguides.app.twosigma.com) for existing categories)
5. Provide a short description of your documentation for "TS documentation description"
6. Check the box for "TS documentation display on landing" if you want your entry to appear on the [TS Guide landing page](https://tsguides.app.twosigma.com)

# Alternative: Auto-deploy from the Monorepo

If your documentation lives in the monorepo (i.e., VATS), you can have your documentation auto-deploy on push. To make use of this workflow, do the following:

## Configure Makefile

Create a Makefile in the same directory that contains your `mdoc` documentation directory with the following:

```
TOP := .
TOOLS := $(shell /usr/libexec/gettools ${TOP})

PROJECTS := bin

TS_MDOC_HOME := $(shell ${TOOLS}/bin/buildpath -H ts_testing_documenting)

include ${TOOLS}/mk/ts.master.nr.mk

.PHONY: mdoc

mdoc:
        ${TS_MDOC_HOME}/bin/mdoc --doc-id <add-your-doc-id-here> --source mdoc --deploy dist/mdoc

mdoc_clean:
        ${_RMRF} ${TOP}/dist/mdoc

all: mdoc
clean: mdoc_clean
```

## Opt-in in CMDB

The [ts_documentation entity](https://cmdb.twosigma.com/entity_types/ts_documentation) has an optional field, `ts_documentation_auto_update_on_push`. 

When [registering your documentation](deployment/workflows#register-documentation), set this boolean field to `true`.
