/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    const WrapperComponent = ({elementsLibrary, content}) => (
        <div className={className}>
            <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={content}/>
        </div>
    );

    WrapperComponent.displayName = `WrapperOnly(${className})`;
    return WrapperComponent;
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
