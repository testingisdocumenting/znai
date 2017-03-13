import React from 'react'
import EmbeddedAnnotatedImage from './EmbeddedAnnotatedImage'

const data = {imageSrc: 'ui.jpg',
    shapes: [
        {type: 'circle', id: 'c1', x: 100, y: 100, r: 20, text: '1'},
        {type: 'circle', id: 'c2', x: 180, y: 100, r: 20, text: '2', color: 'blue'},
        {type: 'circle', id: 'c3', x: 150, y: 150, r: 30, text: '3', color: 'green'},
        {type: 'arrow', id:'a1', beginX: 200, beginY: 200, endX: 300, endY: 300, color: 'yellow', text:'This here'}]
}

const EmbeddedAnnotatedImageDemo = () => {
    const style = {padding: 50}
    return (<div>
        <div style={style}>test text<br/>test text<br/>test text<br/>test text<br/></div>
        <EmbeddedAnnotatedImage {...data}/>
    </div>)}

export default EmbeddedAnnotatedImageDemo