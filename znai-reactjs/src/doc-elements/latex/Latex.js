import React from 'react'

import * as katex from 'katex'
import 'katex/dist/katex.min.css';

import './Latex.css'

export default class Latex extends React.Component {
    render() {
        return (
            <div className="latex content-block" ref={this.saveRef}/>
        )
    }

    saveRef = (node) => {
        this.node = node
    }

    katexRender() {
        const {latex} = this.props
        katex.render(latex, this.node, {
            throwOnError: false
        });
    }

    componentDidUpdate() {
        this.katexRender()
    }

    componentDidMount() {
        this.katexRender()
    }
}