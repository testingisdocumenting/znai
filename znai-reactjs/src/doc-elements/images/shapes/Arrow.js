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
import colorByName from './colorByName'

const ArrowBody = ({beginX, beginY, endX, endY, color, text, ...props}) => {
    const colorScheme = colorByName(color)
    return (
        <g {...props}>
            <defs>
                <marker id="arrow" markerWidth="10" markerHeight="10" opacity="0.4" refX="0" refY="2" orient="auto" markerUnits="strokeWidth">
                    <path d="M0,0 L0,4 L2,2 z" fill={colorScheme.stroke} />
                </marker>
            </defs>
            <line x1={beginX} y1={beginY} x2={endX} y2={endY} fill={colorScheme.fill} stroke={colorScheme.stroke} strokeWidth="8" strokeOpacity="0.4" markerEnd="url(#arrow)" />
            <text x={beginX}
                  y={ (beginY < endY) ? beginY - 15 : beginY + 15 }
                  fill={colorScheme.text} textAnchor="middle" alignmentBaseline="central">{text}</text>
        </g>
    );
}

const arrow = {
    body: ArrowBody,

    knobs: shape => {
        const tail = {id: "tail", x: shape.beginX , y: shape.beginY};
        const tip = {id: "tip", x: shape.endX, y: shape.endY};
        return [tail, tip]
    },

    update: (shape, knobId, dx, dy) => {
        switch (knobId) {
            case 'body':
                return {...shape,
                    beginX: shape.beginX + dx,
                    beginY: shape.beginY + dy,
                    endX: shape.endX + dx,
                    endY: shape.endY + dy
                }
            case 'tail':
                return {...shape,
                    beginX: shape.beginX + dx,
                    beginY: shape.beginY + dy,
                }
            case 'tip':
                return {...shape,
                    endX: shape.endX + dx,
                    endY: shape.endY + dy,
                }
            default:
                return {...shape}
        }
    }
}

export default arrow