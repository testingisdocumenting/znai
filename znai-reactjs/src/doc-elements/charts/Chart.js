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
import Pie from './Pie'
import Bar from './Bar'
import Line from './Line'

import {isAllAtOnce} from '../meta/meta'

const types = {Pie, Bar, Line}

const Chart = (props) => {
    const {chartType} = props
    const ChartComponent = chartByType(chartType)

    return (
        <div className="content-block">
            <ChartComponent {...props}/>
        </div>
    )
}

function chartByType(type) {
    if (types.hasOwnProperty(type)) {
        return types[type]
    }

    return Bar
}

const presentationChartHandler = {component: Chart,
    numberOfSlides: ({data, chartType, meta}) => chartType === 'Line' || isAllAtOnce(meta) ? 1 : data.length }

export {Chart, presentationChartHandler}
