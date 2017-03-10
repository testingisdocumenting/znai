import React, {Component} from 'react'

const Draggable = Annotation => class DraggableAnnotation extends Component {
    constructor(props) {
        super(props)
        this.state = {...props}

        this.onDragStarted = this.onDragStarted.bind(this)
        this.onDragStopped = this.onDragStopped.bind(this)
        this.onDrag = this.onDrag.bind(this)
    }

    render() {
        return <Annotation {...this.state} refs={c => this.wrapped = c}/>
    }

    onDragStarted() {
        console.log("started")
        this.setState({dragStarted: true})
    }

    onDragStopped() {
        console.log("stopped")
        this.setState({dragStarted: false})
    }

    onDrag(e) {
        const {dragStarted} = this.state
        if (! dragStarted) {
            return
        }

        console.log("drag", e)
        this.wrapped.updateFromDrag(e)
        // this.setState({x: e.clientX, y: e.layerY})
    }
}

export default Draggable