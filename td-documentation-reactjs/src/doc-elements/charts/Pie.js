import React from 'react'
import {VictoryPie, VictoryTheme} from 'victory'

import defaultStyle from './defaultStyle'

const Pie = ({data, width, height, ...props}) => {
    const calculatedWidth = width || defaultStyle.pie.width
    const calculatedHeight = height || defaultStyle.pie.height

    return (
        <div style={{width: calculatedWidth, height: calculatedHeight}}>
            <VictoryPie data={data}
                        x={0} y={1}
                        width={calculatedWidth} height={calculatedHeight}
                        {...props}
                        theme={VictoryTheme.material}/>
        </div>
    )
}

export default Pie
