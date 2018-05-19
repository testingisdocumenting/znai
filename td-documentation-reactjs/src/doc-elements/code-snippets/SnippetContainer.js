import * as React from 'react'

import './SnippetContainer.css'

class SnippetContainer extends React.Component {
    render() {
        return this.props.wide ?
            this.renderWideMode() : this.renderNormalMode()
    }

    renderNormalMode() {
        const {title} = this.props

        return (
            <div className="snippet-container content-block">
                <Title title={title}/>

                <div className={this.snippetClassName}>
                    <this.props.snippetComponent {...this.props}/>
                </div>
            </div>
        )
    }

    renderWideMode() {
        const {title} = this.props

        const wideModePadding = <div className="padding">&nbsp;</div>

        const className = "snippet-container wide-screen" + (title ? " with-title" : "")
        return (
            <div className={className}>
                {wideModePadding}
                {title && <div className="title-layer">
                    <Title title={title}/>       
                </div>}
                {wideModePadding}
                
                <div className={this.snippetClassName}>
                    <this.props.snippetComponent {...this.props}/>
                </div>
            </div>
        )
    }

    get snippetClassName() {
        const {title} = this.props
        return "snippet" + (title ? " with-title" : "")
    }
}

function Title({title}) {
    if (!title) {
        return null
    }

    return (
        <div className="title-container content-block">
            <div className="title">{title}</div>
        </div>
    )
}

export default SnippetContainer
