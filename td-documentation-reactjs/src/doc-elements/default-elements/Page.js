import React, {Component} from 'react'
import DocElement from './DocElement'
import {presentationRegistry} from '../presentation/PresentationRegistry'

const PresentationTitle = ({tocItem}) => {
    return <h1 className="presentation-title">{tocItem.pageTitle}</h1>
}

const Page = (elementsLibrary) => class Page extends Component {
    constructor(props) {
        super(props)
        presentationRegistry.register(PresentationTitle, {...props}, 1)
    }

    render() {
        const {tocItem, content} = this.props
        return (<div className="page-content">
            <div className="page-title-block">
                <span className="page-title">{tocItem.pageTitle}</span>
            </div>
            <DocElement key={tocItem.pageTitle} content={content} elementsLibrary={elementsLibrary}/>
        </div>)
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.previewEnabled ||
            this.props.tocItem.dirName !== nextProps.tocItem.dirName ||
            this.props.tocItem.fileName !== nextProps.tocItem.fileName
    }
}

export default Page