import React from 'react'
import {VictoryChart, VictoryBar, VictoryAxis, Bar, VictoryTheme} from 'victory'

import defaultStyle from './defaultStyle'

import {CustomLabel, isHidden, hiddenStyle} from './chartPresentation'

const CustomBar = (props) => {
    return isHidden(props) ?
        <Bar {...props} style={hiddenStyle}/>:
        <Bar {...props}/>
}

const BarChart = ({data, width, height, slideIdx, isPresentation, ...props}) => {
    return (
        <div style={{width: width || defaultStyle.bar.width, height: height || defaultStyle.bar.height}}>
            <VictoryChart theme={VictoryTheme.material}
                          domainPadding={{x: 50, y: [20, 20]}}>
                <VictoryAxis crossAxis/>
                <VictoryAxis crossAxis dependentAxis/>
                <VictoryBar data={data} x={0} y={1}
                            dataComponent={<CustomBar isPresentation={isPresentation} slideIdx={slideIdx}/>}
                            labelComponent={<CustomLabel isPresentation={isPresentation} slideIdx={slideIdx}/>}
                            {...props}/>
            </VictoryChart>
        </div>
    )
}

export default BarChart
