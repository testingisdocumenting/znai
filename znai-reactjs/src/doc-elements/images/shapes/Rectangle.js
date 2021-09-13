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

const RectangleBody = ({x, y, width, height, color, text, scale, ...props}) => {
    const styleScheme = styleByName(color)

    const scaledX = x * scale;
    const scaledY = y * scale;

    return (
        <g>
            <rect x={scaledX} y={scaledY} width={width} fill={styleScheme.fill} stroke={styleScheme.line} height={height} strokeWidth={styleScheme.lineWidth} fillOpacity={1} {...props} />
            { text ? <text x={scaledX + width/2} y={scaledY + height/2} fill={styleScheme.text} textAnchor="middle" alignmentBaseline="central">{text}</text> : null }
        </g>
    );
}

const rectangle = {
    body: RectangleBody,

    knobs: shape => {
        const topLeft = {id: "topLeft", x: shape.x , y: shape.y};
        const topRight = {id: "topRight", x: shape.x + shape.width , y: shape.y};
        const bottomRight = {id: "bottomRight", x: shape.x + shape.width, y: shape.y + shape.height};
        const bottomLeft = {id: "bottomLeft", x: shape.x, y: shape.y + shape.height};
        return [topLeft, topRight, bottomLeft, bottomRight]
    },

    update: (shape, knobId, dx, dy) => {
        switch (knobId) {
            case 'body':
                return {...shape, x: shape.x + dx, y: shape.y + dy}
            case 'topLeft':
                return {...shape,
                    width: shape.width - dx,
                    height: shape.height - dy,
                    x: shape.x + dx,
                    y: shape.y + dy
                }
            case 'topRight':
                return {...shape,
                    width: shape.width + dx,
                    height: shape.height - dy,
                    y: shape.y + dy
                }
            case 'bottomRight':
                return {...shape,
                    width: shape.width + dx,
                    height: shape.height + dy,
                }
            case 'bottomLeft':
                return {...shape,
                    width: shape.width - dx,
                    height: shape.height + dy,
                    x: shape.x + dx
                }
            default:
                return {...shape}
        }
    }
}

export default rectangle