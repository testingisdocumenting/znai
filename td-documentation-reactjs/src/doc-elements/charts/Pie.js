import React from 'react'
import {VictoryPie, VictoryTheme, Slice} from 'victory'

import defaultStyle from './defaultStyle'
import {CustomLabel, isHidden, hiddenStyle} from './chartPresentation'

const CustomSlice = (props) => {
    return isHidden(props) ?
        <Slice {...props} style={hiddenStyle}/>:
        <Slice {...props}/>
}

const Pie = ({data, width, height, slideIdx, isPresentation, ...props}) => {
    const calculatedWidth = width || defaultStyle.pie.width
    const calculatedHeight = height || defaultStyle.pie.height

    return (
        <div style={{width: calculatedWidth, height: calculatedHeight}}>
            <VictoryPie data={data}
                        x={0} y={1}
                        dataComponent={<CustomSlice isPresentation={isPresentation} slideIdx={slideIdx}/>}
                        labelComponent={<CustomLabel isPresentation={isPresentation} slideIdx={slideIdx}/>}
                        width={calculatedWidth} height={calculatedHeight}
                        {...props}
                        theme={VictoryTheme.material}/>
        </div>
    )
}

export default Pie
