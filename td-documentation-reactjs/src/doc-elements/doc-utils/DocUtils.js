import React from 'react'

import './DocUtils.css'

const DocUtilsDesc = WrapperOnly('doc-utils-desc content-block')
const DocUtilsDescName = WrapperOnly('doc-utils-desc-name')
const DocUtilsDescAnnotation = WrapperOnly('doc-utils-desc-annotation')
const DocUtilsDescAddname = WrapperOnly('doc-utils-desc-add-name')
const DocUtilsDescSignature = WrapperOnly('doc-utils-desc-signature')
const DocUtilsDescParameterlist = WrapperOnly('doc-utils-desc-parameter-list')
const DocUtilsDescParameter = WrapperOnly('doc-utils-desc-parameter')
const DocUtilsDescOptional = WrapperOnly('doc-utils-desc-optional')
const DocUtilsDescContent = WrapperOnly('doc-utils-desc-content')
const DocUtilsFieldList = WrapperOnly('doc-utils-field-list')
const DocUtilsFieldName = WrapperOnly('doc-utils-field-name')
const DocUtilsFieldBody = WrapperOnly('doc-utils-field-body')

function DocUtilsField({elementsLibrary, content}) {
    return (
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={content}/>
    )
}

function WrapperOnly(className) {
    return ({elementsLibrary, content}) => <div className={className}>
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={content}/>
    </div>
}

export function registerDocUtilsElements(elementsLibrary) {
    const components = {
        DocUtilsDesc,
        DocUtilsDescName,
        DocUtilsDescAnnotation,
        DocUtilsDescAddname,
        DocUtilsDescSignature,
        DocUtilsDescParameterlist,
        DocUtilsDescParameter,
        DocUtilsDescOptional,
        DocUtilsDescContent,
        DocUtilsFieldList,
        DocUtilsField,
        DocUtilsFieldName,
        DocUtilsFieldBody
    }

    Object.keys(components).forEach(k => elementsLibrary[k] = components[k])
}
