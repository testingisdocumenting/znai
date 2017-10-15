import React from 'react'
import Pie from './Pie'
import Bar from './Bar'
import Line from './Line'

import {isAllAtOnce} from '../meta/meta'

const types = {Pie, Bar, Line}

const Chart = (props) => {
    const {chartType} = props
    const ChartComponent = chartByType(chartType)

    return (
        <div className="content-block">
            <ChartComponent {...props}/>
        </div>
    )
}

function chartByType(type) {
    if (types.hasOwnProperty(type)) {
        return types[type]
    }

    return Bar
}

const presentationChartHandler = {component: Chart,
    numberOfSlides: ({data, chartType, meta}) => chartType === 'Line' || isAllAtOnce(meta) ? 1 : data.length }

export {Chart, presentationChartHandler}
