import React from 'react'
import colorByName from './colorByName'

const CircleBody = ({x, y, r, color, text, ...props}) => {
    const colorScheme = colorByName(color)

    return (
        <g>
           <circle cx={x} cy={y} r={r} stroke={colorScheme.stroke} strokeWidth="4" fill={colorScheme.fill} strokeOpacity={0.9} fillOpacity={0.6} {...props}/>
           <text x={x} y={y} fill={colorScheme.text} textAnchor="middle" alignmentBaseline="central">{text}</text>
        </g>
    );
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