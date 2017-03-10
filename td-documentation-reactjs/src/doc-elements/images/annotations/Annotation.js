import React, {Component} from 'react'

const annotation = (AnnotationMeta) => class Annotation extends Component {
    constructor(props) {
        super(props)
        console.log(AnnotationMeta)

        this.meta = new AnnotationMeta({...props,
            onChange: (data) => {
                console.log(data)
                this.setState({...data})
            }});
    }

    render() {
        const elements = [this.meta.body(), ...this.meta.knobs()]

        return <g>
            {elements}
        </g>
    }
}

export default annotation