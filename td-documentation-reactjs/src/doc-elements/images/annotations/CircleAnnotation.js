import React from 'react'

class CircleAnnotation {
    constructor({x, y, r, onChange}) {
        this.x = x
        this.y = y
        this.r = r
        this.onChange = onChange
        this.update = this.update.bind(this)
    }

    knobs() {
        const centerKnob = {id: "center", x: this.x, y: this.y, onPosChange: this.update};
        const rightKnob = {id: "right", x: this.x + this.r, y: this.y, onPosChange: this.update};
        return [centerKnob, rightKnob]
    }

    body() {
        return <circle key="main" cx={this.x} cy={this.y} r={this.r} stroke="#606060" fill="#ccc" opacity={0.4}/>
    }

    update({id, x, y}) {
        if (id === 'center') {
            this.x = x
            this.y = y
        } else if (id === 'right') {
            this.r = Math.abs(this.x - x)
        }

        this.onChange(this)
    }
}

export default CircleAnnotation