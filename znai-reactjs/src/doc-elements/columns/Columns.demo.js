/*
 * Copyright 2020 znai maintainers
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

import {elementsLibrary} from '../DefaultElementsLibrary'
import {Columns} from './Columns'
import {ViewPortProvider} from "../../theme/ViewPortContext";

export function columnsDemo(registry) {
    registry
        .add('half half', () => <Columns elementsLibrary={elementsLibrary} {...contentAndConfig()}/>)
        .add('half half mobile', () => (
            <ViewPortProvider isMobileForced={true}>
                <Columns elementsLibrary={elementsLibrary} {...contentAndConfig()}/>
            </ViewPortProvider>
        ))
        .add('with code snippet', () => <Columns elementsLibrary={elementsLibrary} {...snippetAndText()}/>)
        .add('with code snippet mobile', () => (
            <ViewPortProvider isMobileForced={true}>
                <Columns elementsLibrary={elementsLibrary} {...snippetAndText()}/>
            </ViewPortProvider>
        ));
}

function contentAndConfig() {
    return {
        config: {
            border: true,
            left: {portion: 10}
        },
        columns: [
            {
                content: [
                    {
                        "text": `It_is_very_`,
                        "type": "SimpleText"
                    },
                ]
            },
            {
                content: [
                    {
                        "text": "one liner",
                        "type": "SimpleText"
                    }
                ]
            }]
    }
}

function snippetAndText() {
    return {
        config: {
            left: {portion: 10}
        },
        columns: [
            {
                content: [
                    {
                        "text": "multi line text multi line text multi line text multi line text " +
                            "multi line text multi line text multi line text multi line text " +
                            "multi line text multi line text multi line text multi line text " +
                            "multi line text multi line text multi line text multi line text " +
                            "multi line text multi line text multi line text multi line text",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                content: [
                    {
                        "snippet": "function test() {\n" +
                            "  return null;\n" +
                            "}\n",
                        "type": "Snippet",
                        "lang": "javascript"
                    },
                ]
            }]
    }
}

