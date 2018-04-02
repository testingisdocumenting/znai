import React from 'react'
import ApiParameters from './ApiParameters'
import {elementsLibrary} from '../DefaultElementsLibrary'

const parameters = [
    {name: 'firstName', type: 'string', description: [{"text": "first name", "type": "SimpleText"}]},
    {name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
]

export function apiParametersDemo(registry) {
    registry
        .add('list of parameters', <ApiParameters elementsLibrary={elementsLibrary} parameters={parameters}/>)
}

