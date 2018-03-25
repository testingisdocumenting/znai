import React from 'react'
import colorByName from './colorByName'

const RectangleBody = ({x, y, width, height, color, text, ...props}) => {
    const colorScheme = colorByName(color)
    return (
        <g>
            <rect x={x} y={y} width={width} fill={colorScheme.fill} stroke={colorScheme.stroke} height={height} strokeWidth="4" fillOpacity={0.4} {...props} />
            { text ? <text x={x + width/2} y={y + height/2} fill={colorScheme.text} textAnchor="middle" alignmentBaseline="central">{text}</text> : null }
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