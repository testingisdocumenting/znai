import React from 'react'
import EmbeddedAnnotatedImage from './EmbeddedAnnotatedImage'

const data = {imageSrc: 'ui.jpg',
    shapes: [{type: 'circle', id: 'c1', x: 100, y: 100, r: 50},
        {type: 'circle', id: 'c2', x: 150, y: 150, r: 30}]
}

const EmbeddedAnnotatedImageDemo = () => {
    const style = {padding: 50}
    return (<div>
        <div style={style}>test text<br/>test text<br/>test text<br/>test text<br/></div>
        <EmbeddedAnnotatedImage {...data}/>
    </div>)}

export default EmbeddedAnnotatedImageDemo