import React from 'react'

const Link = ({url, ...props}) => {
    return (<a href={url}><props.elementsLibrary.DocElement {...props}/></a>)
}

export default Link
