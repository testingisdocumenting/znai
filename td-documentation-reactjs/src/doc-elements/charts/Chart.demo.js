import React from 'react'
import {Chart} from './Chart'

const data = [
    ["2011", 10],
    ["2018", 140],
    ["2022", 30],
    ["2032", 80],
    ["2042", 20],
]

export function chartDemo(registry) {
    registry
        .add('pie', <Chart data={data} chartType="Pie"/>)
        .add('pie with inner radius', <Chart data={data} chartType="Pie" innerRadius={100}/>)
        .add('bar', <Chart data={data} chartType="Bar"/>)
        .add('bar horizontal', <Chart data={data} chartType="Bar" horizontal/>)
}
