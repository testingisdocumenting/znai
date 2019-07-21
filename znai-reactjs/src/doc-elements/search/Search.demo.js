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

import React from "react"
import {getSearchPromise} from "./searchPromise"
import SearchPopup from "./SearchPopup"
import {elementsLibrary} from '../DefaultElementsLibrary'

import './testData'

const buttonStyle = {
    border: "1px solid",
    padding: 10,
    cursor: "pointer",
    width: 200
}

class SearchDemo extends React.Component {
    state = {isOpen: true}

    render() {
        const { isOpen } = this.state

        return isOpen ? (
            <SearchPopup searchPromise={getSearchPromise({id: ""})}
                         onClose={this.onClose}
                         elementsLibrary={elementsLibrary}/>
        ) : (
            <div style={buttonStyle} onClick={this.onShowClick}>Show Search</div>
        )
    }

    onClose = () => {
        this.setState({isOpen: false})
    }

    onShowClick = () => {
        this.setState({isOpen: true})
    }
}

export function searchPopupDemo(registry) {
    registry.add('search popup', SearchDemo)
}