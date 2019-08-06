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

class SearchBox extends Component {
    constructor(props) {
        super(props)
        this.state = {value: ""}
    }

    render() {
        return (
            <div className="mdoc-search-popup-input-box">
                <input
                    ref={(dom) => this.dom = dom}
                    placeholder="Type terms to search..."
                    onKeyDown={this.onKeyDown}
                    value={this.state.value}
                    onChange={this.onInputChange}/>
            </div>
        )
    }

    componentDidMount() {
        this.dom.focus();
    }

    // TODO debounce?
    onInputChange = (e) => {
        const value = e.target.value
        this.props.onChange(value)
        this.setState({value})
    }

    onKeyDown = (e) => {
        if (e.key === 'ArrowUp' || e.key === 'ArrowDown') {
            e.preventDefault()
        }
    }
}

export default SearchBox
