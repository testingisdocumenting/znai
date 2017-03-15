import React, {Component} from 'react'
import DocElement from './DocElement'

const Page = (elementsLibrary) => class Page extends Component {
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