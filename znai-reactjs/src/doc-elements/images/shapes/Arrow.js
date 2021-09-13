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
import {styleByName} from './styleByName';

const ArrowBody = ({beginX, beginY, endX, endY, color, text, scale, ...props}) => {
    const styleScheme = styleByName(color, true)

    const scaledBx = beginX * scale;
    const scaledBy = beginY * scale;
    const scaledEx = endX * scale;
    const scaledEy = endY * scale;

    return (
        <g {...props}>
            <defs>
                <marker id="arrow" markerWidth="10" markerHeight="10" opacity="1" refX="0" refY="2" orient="auto" markerUnits="strokeWidth">
                    <path d="M0,0 L0,4 L2,2 z" fill={styleScheme.fill} />
                </marker>
            </defs>
            <line x1={scaledBx} y1={scaledBy} x2={scaledEx} y2={scaledEy} fill={styleScheme.fill} stroke={styleScheme.line} strokeWidth="8" strokeOpacity="1" markerEnd="url(#arrow)" />
            <text x={scaledBx}
                  y={ (scaledBy < scaledEy) ? scaledBy - 15 : scaledBy + 15 }
                  fill={styleScheme.text} textAnchor="middle" alignmentBaseline="central">{text}</text>
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