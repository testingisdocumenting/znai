import React from 'react'

import './Columns.css'

const Columns = ({columns, config, ...props}) => {
    const leftStyle = buildStyle(config.left)
    const rightStyle = buildStyle(config.right)

    const leftClassName = "column" + (config.border ? " border" : "")

    return <div className="columns content-block">
        <div className={leftClassName} style={leftStyle}>
            <props.elementsLibrary.DocElement {...props} content={columns[0].content}/>
        </div>
        <div className="column" style={rightStyle}>
            <props.elementsLibrary.DocElement {...props} content={columns[1].content}/>
        </div>
    </div>
}

function buildStyle(columnConfig) {
    const style = {}

    if (! columnConfig) {
        return style
    }

    if (columnConfig.width) {
        style.maxWidth = columnConfig.width
    }

    if (columnConfig.align) {
        style.textAlign = columnConfig.align
    }

    return style
}

const presentationColumnsHandler = {component: Columns,
    numberOfSlides: ({columns}) => columns.length}

export {Columns, presentationColumnsHandler}
