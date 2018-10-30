import React, {Component} from 'react'
import scrollIntoView from 'scroll-into-view-if-needed'

export default class SearchTocItem extends Component {
    render() {
        const {pageTitle, pageSection, isSelected, idx, onSelect, onJump} = this.props

        const className = "mdoc-search-toc-item" + (isSelected ? " selected" : "")

        return (
            <div className={className}
                 onClick={() => onSelect(idx)}
                 onDoubleClick={() => onJump(idx)}
                 ref={this.saveRef}>
                <span className="mdoc-search-toc-page-title">{pageTitle}</span>
                <span className="mdoc-search-toc-section-title">{pageSection}</span>
            </div>
        )
    }

    saveRef = (node) => {
        this.node = node
    }

    componentDidMount() {
        this.scrollIfRequired()
    }

    componentDidUpdate() {
        this.scrollIfRequired()
    }

    scrollIfRequired() {
        const {isSelected} = this.props

        if (isSelected) {
            scrollIntoView(this.node, { behavior: 'smooth', scrollMode: 'if-needed' })
        }
    }
}