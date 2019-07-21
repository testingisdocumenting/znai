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

import React, {Component} from 'react'
import Json from './Json'

class JsonDemo extends Component {
    render() {
        const simpleData = 'test'
        const arraySimpleData = ['word', 'red', 'another']
        const numArraySimpleData = [10, 100, 20]
        const objectSimpleData = {'key1': 'value1 "quote" part', 'key2': 'value2'}
        const objectNestedData = {'key1': 'value1', 'key2': {'key21': 'value21', 'key22': 'value22', 'key23': {'key231': 'value231'}}, 'key3': {'key31': 'value31', 'key32': 'value32'}}
        const arrayOfObjectWithinObjectData = {'accounts': [{name: 'ta1', amount: 200}, {name: 'ta2', amount: 150}]}
        const arrayOfObject = [{name: 'ta1', amount: 200}, {name: 'ta2', amount: 150}]

        return  (
            <div>
                <p><i>simple value</i></p>
                <Json data={simpleData}/>
                <br/>
                <p><i>array of simple values</i></p>
                <Json data={arraySimpleData} highlightedPaths={["body[1]"]}/>
                <br/>
                <p><i>array of simple number values</i></p>
                <Json data={numArraySimpleData} highlightedPaths={["body[2]"]}/>
                <br/>
                <p><i>record</i></p>
                <Json data={objectSimpleData}/>
                <p><i>nested record</i></p>
                <Json data={objectNestedData} highlightedPaths={["body.key2.key22", "body.key3.key31"]}/>
                <br/>
                <p><i>array of records within object</i></p>
                <Json data={arrayOfObjectWithinObjectData}/>
                <br/>
                <p><i>array of records</i></p>
                <Json data={arrayOfObject}/>
                <br/>
            </div>
        )
    }
}

export default JsonDemo
