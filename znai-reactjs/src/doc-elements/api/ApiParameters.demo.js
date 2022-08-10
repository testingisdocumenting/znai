/*
 * Copyright 2020 znai maintainers
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

import React from "react";
import ApiParameters from "./ApiParameters";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { Paragraph } from "../paragraph/Paragraph";
import { DocElement } from "../default-elements/DocElement";

const personParameters = [
    {anchorId: 'prefix_firstName', name: 'firstName', type: 'string', description: [{"text": "first name", "type": "SimpleText"}]},
    {anchorId: 'prefix_lastName',name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
    {anchorId: 'prefix_score',name: 'score', type: 'integer', description: [{"text": "score accumulated over last year", "type": "SimpleText"}]},
]

const singleParameter = [
    {anchorId: 'prefix_firstName', name: 'firstName', type: 'string', description: [{"text": "first name", "type": "SimpleText"}]},
]

const personParametersWithTypeRef = [
    {anchorId: 'prefix_firstName', name: 'firstName', type: [
            {text: "list of ", refId: ""},
            {text: "tokens", refId: "lib_string_tokens"}],
        description: [{"text": "first name", "type": "SimpleText"}]},
    {anchorId: 'prefix_lastName',name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
    {anchorId: 'prefix_score',name: 'score', type: 'integer', description: [{"text": "score accumulated over last year", "type": "SimpleText"}]},
]

const paragraph = {"type": "Paragraph", "content": [{"text": "first name", "type": "SimpleText"}]}
const code = {"type": "Snippet", "snippet": "println 'hello world'"}

const multipleParagraph = [paragraph, paragraph, paragraph]
const codeBeforeParagraph = [code, paragraph, code, paragraph, paragraph]

const personLongTypeParameters = [
    {name: 'firstName', type: 'string or gstring or some union and some other long description', description: multipleParagraph},
    {name: 'lastName', type: 'string', description: multipleParagraph},
    {name: 'score', type: 'integer', description: multipleParagraph},
]

const personLongDescriptionParameters = [
    {name: 'firstName', type: 'string', description: multipleParagraph},
    {name: 'lastName', type: 'string', description: multipleParagraph},
    {name: 'score', type: 'integer', description: multipleParagraph},
]

const longNameParameters = [
    {anchorId: 'prefix_long1', name: 'VERY_LONG_PARAMETER_NAME_WITHOUT_SPACES', type: 'string', description: [{"text": "first name line long long first name line long long first name line long long", "type": "SimpleText"}]},
    {anchorId: 'prefix_long2',name: 'VERY_LONG_ANOTHER_PARAMETER_NAME_WITHOUT_SPACES', type: 'string', description: [{"text": "last name last name last name last name last name last name last name last name ", "type": "SimpleText"}]},
]

const mailBoxParameters = [
    {name: 'zipCode', type: 'string', description: [{"text": "zip code", "type": "SimpleText"}]},
    {name: 'isPersonal', type: 'boolean', description: [{"text": "does it belong to a org or a person", "type": "SimpleText"}]},
]

const addressParameters = [
    {name: 'street', type: 'string', description: [{"text": "street name", "type": "SimpleText"}]},
    {name: 'mailBox', type: 'object', children: mailBoxParameters,  description: [{"text": "mail box", "type": "SimpleText"}]},
    {name: 'simple', type: 'string', description: [{"text": "simple parameter after a complex one", "type": "SimpleText"}]},
]

const nestedParameters = [
    {name: 'primaryResidence', type: 'object', children: addressParameters, description: [{"text": "primary residence", "type": "SimpleText"}]},
    {name: 'secondaryPerson', type: 'object', children: createPersonParameters('nested'), description: [{"text": "secondary person", "type": "SimpleText"}]},
    {name: 'short', type: 'object', children: createPersonParameters('nested'), description: [{"text": "secondary person", "type": "SimpleText"}]},
    {name: 'ids', type: 'array of objects', children: createPersonParameters('nested'), description: [{"text": "secondary person", "type": "SimpleText"}]},
]

const singleNestedParameters = [
    {name: 'primaryResidence', type: 'object', children: addressParameters, description: [{"text": "primary residence", "type": "SimpleText"}]},
]

const withCodeSnippetFirst = [
    {name: 'paramOne', type: 'string', description: codeBeforeParagraph}
]

const jsonExample = `
{
  "key1": "value1"
}
`

export function apiParametersDemo(registry) {
    registry
      .add('flat parameters', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters}/>
      ))
      .add('flat parameters with example', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} example={jsonExample}/>
      ))
      .add('single parameter', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={singleParameter}/>
      ))
      .add('flat parameters with global refs', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParametersWithTypeRef}/>
      ))
      .add('flat parameters small size', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} small={true}/>
      ))
      .add('flat parameters long type', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personLongTypeParameters}/>
      ))
      .add('with long names without spaces', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={longNameParameters}/>
      ))
      .add('with long names without spaces and noWrap option', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={longNameParameters} noWrap={true}/>
      ))
      .add('flat parameters with title', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} title="Person definition"/>
      ))
      .add('flat parameters with title and example', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} title="Person definition" example={jsonExample}/>
      ))
      .add('collapsible requires title', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} title="Person definition" collapsible={true}/>
      ))
      .add('collapsible collapsed by default', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} title="Person definition" collapsible={true} collapsed={true}/>
      ))
      .add('flat parameters with long description', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={personLongDescriptionParameters}/>
      ))
      .add('flat parameters with references', () => (
        <ApiParameters elementsLibrary={elementsLibrary}
                       parameters={personParameters}
                       references={paramsReferences()}/>
      ))
      .add('nested parameters', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters}/>
      ))
      .add('single nested parameter', () => (
        <ApiParameters elementsLibrary={elementsLibrary} parameters={singleNestedParameters}/>
      ))
      .add('with text around', () => (
        <div className="content-block">
            <React.Fragment>
                <ParagraphText/>
                <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters}/>
                <ParagraphText/>
            </React.Fragment>
        </div>
      ))
      .add('with text around small size', () => (
        <div className="content-block">
            <React.Fragment>
                <ParagraphText/>
                <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters} small={true}/>
                <ParagraphText/>
            </React.Fragment>
        </div>
      ))
      .add('with text around wide mode', () => (
        <DocElement content={apiParametersFullContent()} elementsLibrary={elementsLibrary}/>
      ))
      .add('multiple api parameters', () => (
        <DocElement content={multipleApiParameters()} elementsLibrary={elementsLibrary}/>
      ))
      .add('with text around and title', () => (
        <div className="content-block">
            <ParagraphText/>
            <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters} title="Person definition"/>
            <ParagraphText/>
        </div>
      ))
      .add('with code first in description', () => (
        <div className="content-block">
            <ParagraphText/>
            <ApiParameters elementsLibrary={elementsLibrary} parameters={withCodeSnippetFirst}/>
            <ParagraphText/>
        </div>
      ))
}

function ParagraphText() {
    return (
        <Paragraph content={simpleText()} elementsLibrary={elementsLibrary}/>
    )
}

function simpleText() {
    return [
        {type: 'SimpleText', text: 'simple paragraph with text. simple paragraph with text. simple paragraph with text.'}
    ]
}

function paramsReferences() {
    return {
        'firstName': {
            pageUrl: '#firstname'
        },
        'integer': {
            pageUrl: '#integer'
        }
    }
}

function createPersonParameters(anchorPrefix) {
    return [
        {anchorId: anchorPrefix + '_firstName', name: 'firstName', type: 'string', description: [{"text": "first name", "type": "SimpleText"}]},
        {anchorId: anchorPrefix + '_lastName',name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
        {anchorId: anchorPrefix + '_score',name: 'score', type: 'integer', description: [{"text": "score accumulated over last year", "type": "SimpleText"}]},
    ]
}

function apiParametersFullContent() {
  return [
    {
      type: "Paragraph",
      content: [
        {
          type: "SimpleText",
          text: "hello world"
        }
      ]
    },
    {
      type: "ApiParameters",
      parameters: longNameParameters,
      wide: true,
      noWrap: true,
      title: "my params"
    },
    {
      type: "Paragraph",
      content: [
        {
          type: "SimpleText",
          text: "hello world"
        }
      ]
    }
  ]
}

function multipleApiParameters() {
  return [
    {
      type: "Paragraph",
      content: [
        {
          type: "SimpleText",
          text: "hello world"
        }
      ]
    },
    {
      type: "ApiParameters",
      parameters: longNameParameters,
      noWrap: true,
      title: "my params"
    },
    {
      type: "ApiParameters",
      parameters: longNameParameters,
      noWrap: true,
      title: "my params"
    },
    {
      type: "ApiParameters",
      parameters: longNameParameters,
      noWrap: true,
      title: "my params"
    },
    {
      type: "Paragraph",
      content: [
        {
          type: "SimpleText",
          text: "hello world"
        }
      ]
    }
  ]
}
