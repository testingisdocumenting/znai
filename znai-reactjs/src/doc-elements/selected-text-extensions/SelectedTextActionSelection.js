import React from 'react'

import './SelectedTextActionSelection.css'

const leftMargin = 150
const SelectedTextPluginSelection = ({textSelection, extensions}) => {
    const rect = textSelection.startNode.getBoundingClientRect()

    const style = {
        top: rect.top,
        left: rect.left - leftMargin,
    }

    return (
        <div className="selected-text-plugin-selection" style={style}>
            {extensions.map(({name, action}) => <div key={name} className="extension-action"
                                                     onMouseDown={() => action(textSelection)}>{name}</div> )}
        </div>
    )
}

export default SelectedTextPluginSelection
