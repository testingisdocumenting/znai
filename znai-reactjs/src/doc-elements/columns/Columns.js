/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'

import {useIsMobile} from "../../theme/ViewPortContext";

import './Columns.css'

const Columns = ({columns, config, isPresentation, slideIdx, ...props}) => {
    const isMobile = useIsMobile()

    const leftStyle = buildStyle(config.left)
    const rightStyle = buildStyle(config.right)

    const showRight = !isPresentation || slideIdx >= 1
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

    const className = 'columns content-block' + (isMobile ? ' mobile' : '')
    return (
        <div className={className}>
            {left}
            {right}
        </div>
    )
}

const defaultColumnFlex = 10
function buildStyle(columnConfig) {
    const style = {}

    if (!columnConfig) {
        return {flex: defaultColumnFlex}
    }

    style.flex = columnConfig.portion || defaultColumnFlex

    if (columnConfig.align) {
        style.textAlign = columnConfig.align
    }

    return style
}

const presentationColumnsHandler = {
    component: Columns,
    numberOfSlides: ({columns}) => columns.length
}

export {Columns, presentationColumnsHandler}
