/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'

import {VictoryChart} from 'victory-chart'
import {Bar, VictoryBar} from 'victory-bar'
import {VictoryAxis} from 'victory-axis'

import materialTheme from 'victory-core/lib/victory-theme/material'
import defaultStyle from './defaultStyle'

import {CustomLabel, hiddenStyle, isHidden} from './chartPresentation'

const CustomBar = (props) => {
    return isHidden(props) ?
        <Bar {...props} style={hiddenStyle}/>:
        <Bar {...props}/>
}

const BarChart = ({data, width, height, slideIdx, isPresentation, meta, ...props}) => {
    return (
        <div style={{width: width || defaultStyle.bar.width, height: height || defaultStyle.bar.height}}>
            <VictoryChart theme={materialTheme}
                          domainPadding={{x: 50, y: [20, 20]}}>
                <VictoryAxis crossAxis/>
                <VictoryAxis crossAxis dependentAxis/>
                <VictoryBar data={data} x={0} y={1}
                            dataComponent={<CustomBar isPresentation={isPresentation} meta={meta} slideIdx={slideIdx}/>}
                            labelComponent={<CustomLabel isPresentation={isPresentation} meta={meta} slideIdx={slideIdx}/>}
                            {...props}/>
            </VictoryChart>
        </div>
    )
}

export default BarChart
