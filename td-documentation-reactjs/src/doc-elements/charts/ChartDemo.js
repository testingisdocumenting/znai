import React from 'react'
import Chart from './Chart'

const data = [
    ["2011", 10],
    ["2018", 140],
    ["2022", 30],
    ["2032", 80],
    ["2042", 20],
]

const Demo = () => {
    return (
        <div>
            <Chart data={data} chartType="Pie"/>
            <Chart data={data} chartType="Pie" innerRadius={100}/>
            <Chart data={data} chartType="Bar"/>
            <Chart data={data} chartType="Bar" horizontal/>
        </div>
    )
}

export default Demo
