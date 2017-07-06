import React from 'react'
import VerticalBarChart from './VerticalBarChart'

const data = [
    ["2011", 10],
    ["2018", 140],
    ["2022", 30],
    ["2032", 80],
    ["2042", 20],
]

const VerticalBarChartDemo = () => {
    return <VerticalBarChart data={data}/>
}

export default VerticalBarChartDemo
