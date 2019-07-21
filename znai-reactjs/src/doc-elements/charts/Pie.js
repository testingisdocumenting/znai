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
