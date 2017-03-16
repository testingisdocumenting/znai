import React, {Component} from 'react'

class Page extends Component {
    render() {
        const {tocItem, content, elementsLibrary} = this.props
        return (<div className="page-content">
            <div className="page-title-block">
                <span className="page-title">{tocItem.pageTitle}</span>
            </div>
            <elementsLibrary.DocElement key={tocItem.pageTitle} content={content}/>
        </div>)
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.previewEnabled ||
            this.props.tocItem.dirName !== nextProps.tocItem.dirName ||
            this.props.tocItem.fileName !== nextProps.tocItem.fileName
    }
}

export default Page