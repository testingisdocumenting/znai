import React from 'react'
import Pie from './Pie'
import Bar from './Bar'

const types = {Pie, Bar}

const Chart = (props) => {
    const {chartType} = props
    const ChartComponent = chartByType(chartType)

    return <ChartComponent {...props}/>
}

function chartByType(type) {
    if (types.hasOwnProperty(type)) {
        return types[type]
    }

    return Bar
}

export default Chart
