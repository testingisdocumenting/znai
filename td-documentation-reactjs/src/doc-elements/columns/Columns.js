import React from 'react'

import './Columns.css'

const Columns = ({elementsLibrary, columns}) => {
    return <div className="columns content-block">
        <div className="column">
            <elementsLibrary.DocElement content={columns[0].content}/>
        </div>
        <div className="column">
            <elementsLibrary.DocElement content={columns[1].content}/>
        </div>
    </div>
}

const presentationColumnsHandler = {component: Columns,
    numberOfSlides: ({columns}) => columns.length}

export {Columns, presentationColumnsHandler}
