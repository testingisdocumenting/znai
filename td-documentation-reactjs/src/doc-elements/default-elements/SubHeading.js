import React from 'react'

const SubHeading = ({level, ...props}) => {
    const Element = `h${level}`
    return <Element className="content-block"><props.elementsLibrary.DocElement {...props}/></Element>
}

export default SubHeading
