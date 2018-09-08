import React from 'react'

import {VictoryChart} from 'victory-chart'
import {VictoryLine} from 'victory-line'
import {VictoryAxis} from 'victory-axis'

import materialTheme from 'victory-core/lib/victory-theme/material'

import defaultStyle from './defaultStyle'

const LineChart = ({data, width, height, ...props}) => {
    return (
        <div style={{width: width || defaultStyle.line.width, height: height || defaultStyle.line.height}}>
            <VictoryChart theme={materialTheme}
                          domainPadding={{x: 50, y: [20, 20]}}>
                <VictoryAxis crossAxis/>
                <VictoryAxis crossAxis dependentAxis/>
                <VictoryLine data={data} x={0} y={1}
                             {...props}/>
            </VictoryChart>
        </div>
    )
}

export default LineChart
