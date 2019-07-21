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

/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text, TextInput,
    View
} from 'react-native';

import testData from './testData'

const styles = StyleSheet.create({
    paragraph: {
        paddingBottom: 10,
    }
});

const library = {};

const DocElement = ({content, elementsLibrary}) => {
    const children = !content ? null : content.map((item, idx) => {
        const ElementToUse = elementsLibrary[item.type]
        console.log("ElementToUse", ElementToUse);
        if (!ElementToUse) {
            console.warn("can't find component to display", item);
            return null
        } else {
            return <ElementToUse key={idx} {...item} elementsLibrary={elementsLibrary}/>
        }
    })

    return (<View>{children}</View>);
};

const BoundDocElement = ({content}) => <DocElement content={content} elementsLibrary={library}/>;

library.DocElement = BoundDocElement;

const Paragraph = ({content}) => {
    return <View style={styles.paragraph}><BoundDocElement content={content}/></View>
}

const SimpleText = ({text}) => <Text>{text}</Text>

library.Paragraph = Paragraph
library.SimpleText = SimpleText

export default class Reader extends Component {
    constructor(props) {
        super(props);
        this.state = {text: ''};
    }

    render() {
        return (
            <View style={{padding: 10}}>
                <BoundDocElement content={testData}/>
            </View>
        );
    }
}

AppRegistry.registerComponent('reader', () => Reader);
