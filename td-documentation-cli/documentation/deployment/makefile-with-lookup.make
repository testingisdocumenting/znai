TOP := .
TOOLS := $(shell /usr/libexec/gettools ${TOP})

PROJECTS := bin

TS_MDOC_HOME := $(shell ${TOOLS}/bin/buildpath -H ts_testing_documenting)

# codebases referenced in my documentation
TS_CODEBASE_ONE := $(shell ${TOOLS}/bin/buildpath -H ts_codebase_one)
TS_CODEBASE_TWO := $(shell ${TOOLS}/bin/buildpath -H ts_codebase_two)
TS_CODEBASE_THREE := $(shell ${TOOLS}/bin/buildpath -H ts_codebase_three)

include ${TOOLS}/mk/ts.master.nr.mk

.PHONY: mdoc

mdoc:
    echo ${TS_CODEBASE_ONE}/java/src > mdoc/lookup-paths
    echo ${TS_CODEBASE_TWO}/java/src >> mdoc/lookup-paths
    echo ${TS_CODEBASE_THREE}/java/src >> mdoc/lookup-paths
    ${TS_MDOC_HOME}/bin/mdoc --doc-id <add-your-doc-id-here> --source mdoc --deploy dist/mdoc

mdoc_clean:
    ${_RMRF} ${TOP}/dist/mdoc

all: mdoc
clean: mdoc_clean