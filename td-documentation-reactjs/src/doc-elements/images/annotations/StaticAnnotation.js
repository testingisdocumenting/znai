import React from 'react'

const staticAnnotation = (shapeHandler) => ({shape}) => {
    const Body = shapeHandler.body;
    return <Body key="body" {...shape}/>
}

export default staticAnnotation