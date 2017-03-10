import React from 'react'
import Knob from './Knob'

class CircleAnnotation {
    constructor({x, y, r, onChange}) {
        this.x = x
        this.y = y
        this.r = r
        this.onChange = onChange
        this.update = this.update.bind(this)
    }

    knobs() {
        const centerKnob = <Knob knobId="center" x={this.x} y={this.y} onPosChange={this.update}/>;
        const rightKnob = <Knob knobId="right" x={this.x + this.r} y={this.y} onPosChange={this.update}/>;
        return [centerKnob, rightKnob]
    }

    body() {
        return <circle cx={this.x} cy={this.y} r={this.r} stroke="#606060" fill="#ccc" opacity={0.4}/>
    }

    update({knobId, x, y}) {
        if (knobId === 'center') {
            this.x = x
            this.y = y
        } else if (knobId === 'right') {
            this.r = Math.abs(this.x - x)
        }

        this.onChange(this)
    }
}

export default CircleAnnotation