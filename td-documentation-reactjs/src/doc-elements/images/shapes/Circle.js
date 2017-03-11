import React from 'react'

const CircleBody = ({x, y, r, ...props}) => {
    return <circle cx={x} cy={y} r={r}
                   stroke="#606060" fill="#ccc" opacity={0.4}
                   {...props}/>
}

const circle = {
    body: CircleBody,

    knobs: shape => {
        const right = {id: "right", x: shape.x + shape.r, y: shape.y};
        const left = {id: "left", x: shape.x - shape.r, y: shape.y};
        return [left, right]
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
