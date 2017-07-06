import React from 'react'

import './Columns.css'

const Columns = ({columns, config, isPresentation, slideIdx, ...props}) => {
    const leftStyle = buildStyle(config.left)
    const rightStyle = buildStyle(config.right)

    const showRight = ! isPresentation || slideIdx >= 1
    const leftClassName = "column" + ((config.border && showRight) ? " border" : "")

    const left = (
        <div className={leftClassName} style={leftStyle}>
            <props.elementsLibrary.DocElement {...props} content={columns[0].content}/>
        </div>
    )

    const right = showRight ? (
        <div className="column" style={rightStyle}>
            <props.elementsLibrary.DocElement {...props} content={columns[1].content}/>
        </div>
    ) : null

    return (
        <div className="columns content-block">
            {left}
            {right}
        </div>
    )
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
