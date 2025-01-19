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
import DiagramLegend from "./DiagramLegend"
import { Paragraph } from "../paragraph/Paragraph"

import {elementsLibrary} from '../DefaultElementsLibrary'

export function diagramLegendDemo(registry) {
    const legend = {a: "core components", b: "external facing"}

    registry
        .add('single line', () => (
            <DiagramLegend legend={legend}/>
        ))
        .add('with clickable nodes message', () => (
            <DiagramLegend legend={legend} clickableNodes={true}/>
        ))
        .add('wrapped with text', () => (
            <React.Fragment>
                <Paragraph elementsLibrary={elementsLibrary} content={[
                    {
                        "text": "Some text before",
                        "type": "SimpleText"
                    }]}/>
                <DiagramLegend legend={legend} clickableNodes={true}/>
                <Paragraph elementsLibrary={elementsLibrary} content={[
                    {
                        "text": "Some text after",
                        "type": "SimpleText"
                    }]}/>
            </React.Fragment>
        ))
}