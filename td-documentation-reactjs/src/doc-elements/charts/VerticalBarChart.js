import React from 'react'
import {VictoryChart, VictoryBar, VictoryAxis} from 'victory'
import {VictoryTheme} from 'victory'

const VerticalBarChart = ({data, width, height}) => {
    return (
        <div style={{width: width || 900, height: height || 500}}>
            <VictoryChart theme={VictoryTheme.material}
                          domainPadding={{x: 50, y: [0, 20]}}>
                <VictoryAxis crossAxis/>
                <VictoryAxis crossAxis dependentAxis/>
                <VictoryBar data={data} x={0} y={1} labelComponent={<span/>}/>
            </VictoryChart>
        </div>
    )
}

export default VerticalBarChart
