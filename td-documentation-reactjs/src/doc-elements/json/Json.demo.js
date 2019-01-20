import React from 'react'
import Json from './Json'
import {TwoSidesLayoutRightPart} from '../page/two-sides/TwoSidesLayout'

const arraySimpleData = ['word', 'red', 'another']
const objectSimpleData = {'key1': 'value1 "quote" part', 'key2': 'value2'}
const objectNestedData = {
    "key1": "value1",
    "key2": {
        "key21": "value21",
        "key22": "value22",
        "key23": {
            "key231": "value231"
        }
    },
    "key3": {
        "key31": 100,
        "key32": "value32"
    },
    "key4": [1, 2, 3, 4, 5]
}

const objectEmptyData = {
    "key1": "value1",
    "key2": {
        "key21": "value21",
        "key22": [],
        "key23": {}
    },
    "key3": [{}, {}],
    "key4": [[], []]
}

const objectNestedDataLongNames = {
    "properties": {
        "code": {
            "type": "integer",
            "format": "int32"
        },
        "message": {
            "type": "string"
        }
    }
}

const arrayOfObjectWithinObjectData = {'accounts': [{name: 'ta1', amount: 200}, {name: 'ta2', amount: 150}]}
const arrayOfObject = [{name: 'ta1', amount: 200}, {name: 'ta2', amount: 150}]

export function jsonDemo(registry) {
    registry
        .add('array of simple', () => <Json data={arraySimpleData} paths={['root[1]']}/>)
        .add('with title', () => <Json data={arraySimpleData} paths={['root[1]']} title="Response"/>)
        .add('record', () => <Json data={objectSimpleData}/>)
        .add('nested record', () => <Json data={objectNestedData} paths={['root.key2.key22', 'root.key3.key31']}/>)
        .add('nested with empty', () => <Json data={objectEmptyData}/>)
        .add('nested record with collapsed entry',
            () => <Json data={objectNestedData}
                        paths={['root.key2.key22', 'root.key3.key31']}
                        collapsedPaths={['root.key2', 'root.key4']}/>)
        .add('nested record right side background', () => <TwoSidesLayoutRightPart><Json data={objectNestedData}
                                                                                         paths={['root.key2.key22', 'root.key3.key31']}/></TwoSidesLayoutRightPart>)
        .add('nested record with collapsed entry right side background', () =>
            <TwoSidesLayoutRightPart>
                <Json data={objectNestedData}
                      paths={['root.key2.key22', 'root.key3.key31']}
                      collapsedPaths={['root.key2', 'root.key4']}/>
            </TwoSidesLayoutRightPart>)
        .add('nested record with long name', () => <Json data={objectNestedDataLongNames}/>)
        .add('array of records within object', () => <Json data={arrayOfObjectWithinObjectData}/>)
        .add('with read more', () => <Json data={arrayOfObjectWithinObjectData} readMore={true}/>)
        .add('with line highlights', () => <Json data={arrayOfObjectWithinObjectData} highlight={[1, 3]}/>)
        .add('array of records', () => <Json data={arrayOfObject}/>)
}
