import React from 'react'
import {VictoryChart, VictoryLine, Curve, VictoryAxis, VictoryTheme} from 'victory'

import defaultStyle from './defaultStyle'

const LineChart = ({data, width, height, ...props}) => {
    return (
        <div style={{width: width || defaultStyle.line.width, height: height || defaultStyle.line.height}}>
            <VictoryChart theme={VictoryTheme.material}
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
