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
import colorByName from "../shapes/colorByName"

const staticAnnotation = (shapeHandler) => ({shape}) => {
    if (!shapeHandler) {
        return <NotFound {...shape}/>
    }

    const Body = shapeHandler.body;
    return <Body key="body" {...shape}/>
}

function NotFound({x, y, width, height, color}) {
    const colorScheme = colorByName(color)

    return (
        <g>
            <rect x={x} y={y} width={width} fill={colorScheme.fill} stroke={colorScheme.line} height={height}
                  strokeWidth="4" fillOpacity={1}/>
            <text x={x + width / 2} y={y + height / 2} fill={colorScheme.text} textAnchor="middle"
                  alignmentBaseline="central">
                &lt;NOT FOUND&gt;
            </text>
        </g>
    )
}

export default staticAnnotation