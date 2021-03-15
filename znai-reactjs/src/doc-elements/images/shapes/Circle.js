/*
 * Copyright 2021 znai maintainers
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
import colorByName from './colorByName'

const CircleBody = ({x, y, r, color, text, align, ...props}) => {
    const colorScheme = colorByName(color)

    const [cx, cy] = calcCenter()

    return (
        <g>
           <circle cx={cx} cy={cy} r={r} stroke={colorScheme.line} strokeWidth="4" fill={colorScheme.fill} strokeOpacity={1} fillOpacity={1} {...props}/>
           <text x={cx} y={cy} fill={colorScheme.text} textAnchor="middle" alignmentBaseline="central">{text}</text>
        </g>
    );

    function calcCenter() {
        const d = r * 2
        return [x + deltaX(), y + deltaY()]

        function deltaX() {
            switch (align) {
                case 'ToTheLeft':
                    return -d
                case 'ToTheRight':
                    return d
                default:
                    return 0
            }
        }

        function deltaY() {
            switch (align) {
                case 'Above':
                    return -d
                case 'Below':
                    return d
                default:
                    return 0
            }
        }
    }
}

const circle = {
    body: CircleBody,

    knobs: shape => {
        const right = {id: "right", x: shape.x + shape.r, y: shape.y};
        return [ right ]
    },

    update: (shape, knobId, dx, dy) => {
        if (knobId === 'body') {
            return {...shape, x: shape.x + dx, y: shape.y + dy}
        } else if (knobId === 'right') {
            return {...shape, r: shape.r + dx}
        }
    }
}

export default circle