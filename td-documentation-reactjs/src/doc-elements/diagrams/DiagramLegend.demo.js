import React from 'react'
import DiagramLegend from "./DiagramLegend"
import Paragraph from "../default-elements/Paragraph"

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