/*
 * Copyright 2022 znai maintainers
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
const Mark = require('mark.js/dist/mark.js')

class SearchPreview extends Component {
    triggerHighlight() {
        this.mark = new Mark(this.dom)
        this.highlight()
    }

    componentDidMount() {
        this.triggerHighlight()
    }

    componentDidUpdate(_prevProp, _prevState) {
        this.triggerHighlight()
    }

    render() {
        const {section, elementsLibrary} = this.props

        return (
            <div className="znai-search-result-preview" ref={(dom) => this.dom = dom}>
                <elementsLibrary.DocElement {...this.props} content={section.content}/>
            </div>
        )
    }

    highlight() {
        const {snippets} = this.props

        this.mark.mark(snippets, {
            acrossElements: false,
            separateWordSearch: true,
            caseSensitive: false,
            ignoreJoiners: false,
            diacritics: false,
            ignorePunctuation: ["(", ")", ";", "[", "]", "-", "_", ".", ",", "\"", "'"],
            accuracy: "exactly",
            done: () => {
                const marked = document.querySelector(".znai-search-result-preview mark");
                if (marked) {
                    marked.scrollIntoView();
                }
            }
        })
    }
}

export default SearchPreview