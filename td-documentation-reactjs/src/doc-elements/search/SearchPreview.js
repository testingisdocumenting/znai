import React, {Component} from 'react'

class SearchPreview extends Component {
    componentDidMount() {
        const Mark = require('mark.js/dist/mark.js') // need to hide from server side rendering
        this.mark = new Mark(this.dom)
        this.highlight()
    }

    componentDidUpdate() {
        this.highlight()
    }

    render() {
        const {section, elementsLibrary} = this.props

        return (
            <div className="mdoc-search-result-preview" ref={(dom) => this.dom = dom}>
                <elementsLibrary.DocElement {...this.props} content={section.content}/>
            </div>
        )
    }

    highlight() {
        const {snippets} = this.props

        this.mark.unmark()
        this.mark.mark(snippets)

        const marked = document.querySelector(".mdoc-search-result-preview mark");
        if (marked) {
            marked.scrollIntoView();
        }
    }
}

export default SearchPreview