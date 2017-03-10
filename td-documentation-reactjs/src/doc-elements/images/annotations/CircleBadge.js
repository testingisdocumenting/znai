import React, {Component} from 'react'

class CircleBadge extends Component {
    constructor(props) {
        super(props)
        this.state = {...props}

        // this.onDragStarted = this.onDragStarted.bind(this)
        // this.onDragStopped = this.onDragStopped.bind(this)
        // this.onDrag = this.onDrag.bind(this)
        //
        // document.addEventListener('mouseup', this.onDragStopped)
        // document.addEventListener('mousemove', this.onDrag)
    }

    render() {
        const {x, y, text, size} = this.state
        return (<circle cx={x} cy={y} r={size} stroke="#606060" fill="#ccc"
                        onMouseDown={this.onDragStarted}/>)
    }

    // onDragStarted() {
    //     console.log("started")
    //     this.setState({dragStarted: true})
    // }
    //
    // onDragStopped() {
    //     console.log("stopped")
    //     this.setState({dragStarted: false})
    // }
    //
    // onDrag(e) {
    //     const {dragStarted} = this.state
    //     if (! dragStarted) {
    //         return
    //     }
    //
    //     console.log("drag", e)
    //     this.setState({x: e.clientX, y: e.layerY})
    // }
}

export default CircleBadge
