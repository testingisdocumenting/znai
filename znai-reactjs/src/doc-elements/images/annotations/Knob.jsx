/*
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
import './Knob.css'

class Knob extends React.Component {
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