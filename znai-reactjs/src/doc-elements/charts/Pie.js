import React from 'react'

import {Slice, VictoryPie} from 'victory-pie'
import materialTheme from 'victory-core/lib/victory-theme/material'

import defaultStyle from './defaultStyle'
import {CustomLabel, hiddenStyle, isHidden} from './chartPresentation'

const CustomSlice = (props) => {
    return isHidden(props) ?
        <Slice {...props} style={hiddenStyle}/>:
        <Slice {...props}/>
}

const Pie = ({data, width, height, slideIdx, isPresentation, meta, ...props}) => {
    const calculatedWidth = width || defaultStyle.pie.width
    const calculatedHeight = height || defaultStyle.pie.height

    return (
        <div style={{width: calculatedWidth, height: calculatedHeight}}>
            <VictoryPie data={data}
                        x={0} y={1}
                        dataComponent={<CustomSlice isPresentation={isPresentation} meta={meta} slideIdx={slideIdx}/>}
                        labelComponent={<CustomLabel isPresentation={isPresentation} meta={meta} slideIdx={slideIdx}/>}
                        width={calculatedWidth} height={calculatedHeight}
                        {...props}
                        theme={materialTheme}/>
        </div>
    )
}

export default Pie
