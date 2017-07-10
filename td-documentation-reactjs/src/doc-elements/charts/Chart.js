import React from 'react'
import Pie from './Pie'
import Bar from './Bar'

const types = {Pie, Bar}

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
    numberOfSlides: ({data}) => data.length }

export {Chart, presentationChartHandler}
