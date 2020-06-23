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
import {getAllPagesPromise} from './allPages'
import {elementsLibrary} from './DefaultElementsLibrary'

import './AllPagesAtOnce.css'

class AllPagesAtOnce extends Component {
    constructor(props) {
        super(props)

        this.state = {allPages: []}
    }

    render() {
        const {docMeta} = this.props
        const {allPages, allPagesRendered} = this.state

        const isLoaded = allPages.length > 0

        return isLoaded ? (
            <div className="all-pages">
                <div className="title content-block">{docMeta.title + " " + docMeta.type}</div>
                {allPagesRendered}
            </div>
        ): (
            <div className="all-pages-loading">
                Building single page view...
            </div>
        )
    }

    componentDidMount() {
        const {docMeta} = this.props
        document.title = docMeta.title

        getAllPagesPromise(docMeta).then(all => {
            const allPages = all.pages
            const allPagesRendered = allPages.map((p, idx) => <elementsLibrary.Page key={idx}
                                                                                    content={p.content}
                                                                                    tocItem={p.tocItem}
                                                                                    docMeta={docMeta}
                                                                                    elementsLibrary={elementsLibrary}/>)
            setTimeout(() => this.setState({allPages, allPagesRendered}), 0);
        })
    }
}

export default AllPagesAtOnce
