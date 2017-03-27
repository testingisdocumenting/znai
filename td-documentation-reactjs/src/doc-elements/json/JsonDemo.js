import React, {Component} from 'react'
import JsonValue from './JsonValue'

class JsonDemo extends Component {
    render() {
        const simpleData = 'test'
        const arraySimpleData = ['word', 'red', 'another']
        const objectSimpleData = {'key1': 'value1', 'key2': 'value2'}
        const objectNestedData = {'key1': 'value1', 'key2': {'key21': 'value21', 'key22': 'value22', 'key23': {'key231': 'value231'}}, 'key3': {'key31': 'value31', 'key32': 'value32'}}
        const arrayOfObjectWithinObjectData = {'accounts': [{name: 'ta1', amount: 200}, {name: 'ta2', amount: 150}]}
        return  (
            <div>
                <p><i>simple value</i></p>
                <JsonValue data={simpleData}/>
                <br/>
                <p><i>array of simple values</i></p>
                <JsonValue data={arraySimpleData} highlightedPaths={{"body[1]": true}}/>
                <br/>
                <p><i>record</i></p>
                <JsonValue data={objectSimpleData}/>
                <p><i>nested record</i></p>
                <JsonValue data={objectNestedData} highlightedPaths={{"body.key2.key22": true}}/>
                <br/>
                <p><i>array of records</i></p>
                <JsonValue data={arrayOfObjectWithinObjectData}/>
                <br/>
            </div>
        )
    }
}

export default JsonDemo
