import React from 'react'

const Box = (props) => {
    return <div className="bullet-box"><props.elementsLibrary.DocElement {...props}/></div>
}

const EmptyBox = () => {
    return <div className="bullet-box-empty"/>
}

const RevealBoxes = ({elementsLibrary, content, slideIdx, ...props}) => {
    const components = Array(content.length).fill().map((nothing, idx) => idx >= slideIdx ? EmptyBox : Box);

    return <div className="bullet-boxes">{content.map((item, idx) => {
        const Component = components[idx]
        return <Component key={idx}
                          {...props}
                          elementsLibrary={elementsLibrary}
                          content={item.content}/>})}</div>
}

export default RevealBoxes
