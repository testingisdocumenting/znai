import React from 'react'

import './FilterInput.css'

export default class FilterInput extends React.Component {
    render() {
        const {filterText, onChange} = this.props

        return (
            <div className="mdoc-landing-filter-input">
                <input ref={this.saveRef}
                       value={filterText}
                       placeholder="Documentations filter..."
                       onChange={onChange}/>
            </div>
        )
    }

    saveRef = (node) => {
        this.node = node
    }

    componentDidMount() {
        this.node.focus()
    }
}