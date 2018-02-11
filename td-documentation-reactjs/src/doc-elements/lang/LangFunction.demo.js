import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'
import LangFunction from './LangFunction'

const nameAndDesc = {
    'name': 'functionName(param1, longerParam2)',
    'description': 'function description',
}

const paramsWithoutType = [
    {
        'name': 'param1',
        'description': 'param1 description',
    },
    {
        'name': 'longerParam2',
        'description': 'longer param2 description'
    }
]

const paramsWithType = [
    {
        'name': 'param1',
        'description': 'param1 description',
        'type': 'str',
    },
    {
        'name': 'longerParam2',
        'description': 'longer param2 description',
        'type': 'integer or None',
    }
]

export function langFunctionDemo(registry) {
    registry
        .add('simple function', <LangFunction {...nameAndDesc} params={paramsWithoutType}
                                              elementsLibrary={elementsLibrary}/>)
        .add('param types', <LangFunction {...nameAndDesc} params={paramsWithType}
                                          elementsLibrary={elementsLibrary}/>)
}
