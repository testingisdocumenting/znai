import React from 'react'
import {VictoryChart, VictoryBar, VictoryAxis, VictoryTheme} from 'victory'

import defaultStyle from './defaultStyle'

const Bar = ({data, width, height, ...props}) => {
    return (
        <div style={{width: width || defaultStyle.bar.width, height: height || defaultStyle.bar.height}}>
            <VictoryChart theme={VictoryTheme.material}
                          domainPadding={{x: 50, y: [20, 20]}}>
                <VictoryAxis crossAxis/>
                <VictoryAxis crossAxis dependentAxis/>
                <VictoryBar data={data} x={0} y={1} labelComponent={<span/>} {...props}/>
            </VictoryChart>
        </div>
    )
}

export default Bar
