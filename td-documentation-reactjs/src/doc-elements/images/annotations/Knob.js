import React, {Component} from 'react'
import './Knob.css'

class Knob extends Component {
    constructor(props) {
        super(props)

        this.state = {dragStarted: false}

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
        const {id, x, y, onMouseOver, onMouseOut} = this.props

        return <circle key={id} className="knob" cx={x} cy={y} r={5}
                       onMouseOver={onMouseOver}
                       onMouseOut={onMouseOut}
                       onMouseDown={this.onDragStarted}/>
    }

    onDragStarted(e) {
        this.lastDragX = e.nativeEvent.offsetX
        this.lastDragY = e.nativeEvent.offsetY

        this.setState({dragStarted: true})
        this.props.onDragStarted()
    }

    onDragStopped() {
        this.setState({dragStarted: false})
        this.props.onDragStopped()
    }

    onDrag(e) {
        const {dragStarted} = this.state
        if (! dragStarted) {
            return
        }

        const newX = e.layerX
        const newY = e.layerY

        const dx = newX - this.lastDragX
        const dy = newY - this.lastDragY

        this.lastDragX = newX
        this.lastDragY = newY

        const {id} = this.props
        this.props.onPosChange({id, dx, dy})
    }
}

export default Knob