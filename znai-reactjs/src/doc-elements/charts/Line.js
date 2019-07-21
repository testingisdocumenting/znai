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
