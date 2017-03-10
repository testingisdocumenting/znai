import React, {Component} from 'react'
import Knob from './Knob'

const annotation = (AnnotationMeta) => class Annotation extends Component {
    constructor(props) {
        super(props)

        this.meta = new AnnotationMeta({...props,
            onChange: (data) => {
                this.setState({...data})
            }});
    }

    render() {
        const elements = [this.meta.body(), ...this.meta.knobs().map(k => <Knob key={k.id} {...k}/>)]

        return <g>
            {elements}
        </g>
    }
}

export default annotation