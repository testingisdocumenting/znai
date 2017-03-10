import React, {Component} from 'react'
import './Knob.css'

class Knob extends Component {
    constructor(props) {
        super(props)
        this.onDragStarted = this.onDragStarted.bind(this)
        this.onDragStopped = this.onDragStopped.bind(this)
        this.onDrag = this.onDrag.bind(this)
    }

    componentDidMount() {
        document.addEventListener('mouseup', this.onDragStopped)
        document.addEventListener('mousemove', this.onDrag)
    }

    componentWillUnmount() {
        document.removeEventListener('mouseup', this.onDragStopped)
        document.removeEventListener('mousemove', this.onDrag)
    }

    render() {
        const {knobId, x, y} = this.props

        return <circle key={knobId} className="knob" cx={x} cy={y} r={5}
                       onMouseOver={this.onMouseOver}
                       onMouseDown={this.onDragStarted}/>
    }

    onDragStarted() {
        this.setState({dragStarted: true})
    }

    onDragStopped() {
        this.setState({dragStarted: false})
    }

    onDrag(e) {
        const {dragStarted} = this.state
        if (! dragStarted) {
            return
        }

        const newX = e.layerX
        const newY = e.layerY

        const {knobId} = this.props
        this.props.onPosChange({knobId: knobId, x: newX, y: newY})
    }
}

export default Knob