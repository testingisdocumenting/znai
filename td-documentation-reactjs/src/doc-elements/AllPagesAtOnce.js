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

        getAllPagesPromise(docMeta).then(allPages => {
            const allPagesRendered = allPages.map((p, idx) => <elementsLibrary.Page key={idx}
                                                                                    content={p.content}
                                                                                    tocItem={p.tocItem}
                                                                                    elementsLibrary={elementsLibrary}/>)
            setTimeout(() => this.setState({allPages, allPagesRendered}), 0);
        })
    }
}

export default AllPagesAtOnce
