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