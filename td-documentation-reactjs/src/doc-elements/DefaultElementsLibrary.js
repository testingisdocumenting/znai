import React from 'react'
import DocElement from './DocElement'
import Snippet from './Snippet'
import GraphVizSvg from './graphviz/GraphVizSvg'
import GraphVizFlow from './graphviz/GraphVizFlow'

class Id {
    constructor() {
        this._id = 0
    }

    next() {
        this._id++
        return this._id
    }
}

const library = {}

const BoundDocElement = ({content, idGen}) => <DocElement content={content} elementsLibrary={library} idGen={idGen}/>

library.DocElement = BoundDocElement
library.Emphasis = ({content, idGen}) => (<span className="emphasis"><BoundDocElement content={content} idGen={idGen}/></span>)
library.StrongEmphasis = ({content, idGen}) => (<span className="strong-emphasis"><BoundDocElement content={content} idGen={idGen}/></span>)
library.Link = ({anchor, label, idGen}) => (<a href={anchor} data-id={idGen.next()}>{label}</a>)
library.Paragraph = ({content, idGen}) => <div className="paragraph content-block" data-id={idGen.next()}><BoundDocElement content={content} idGen={idGen}/></div>
library.BlockQuote = ({content, idGen}) => <blockquote className="content-block"><BoundDocElement content={content} idGen={idGen}/></blockquote>
library.SimpleText = ({text, idGen}) => <span className="simple-text" data-id={idGen.next()}>{text}</span>
library.SoftLineBreak = () => <span> </span>
library.HardLineBreak = () => <br />
library.ThematicBreak = () => <hr />

library.Snippet = Snippet

library.BulletList = ({tight, bulletMarker, content, idGen}) => {
    const className = "content-block" + (tight ? " tight" : "")
    return (<ul className={className}><BoundDocElement content={content} idGen={idGen}/></ul>)
}

library.OrderedList = ({delimiter, startNumber, content, idGen}) => <ol className="content-block" start={startNumber}><BoundDocElement content={content} idGen={idGen}/></ol>

library.ListItem = ({content, idGen}) => <li><BoundDocElement content={content} idGen={idGen}/></li>

library.Section = ({title, content, idGen}) => (
        <div className="section" key={title}>
            <div className="content-block">
                <div className="section-title" data-id={idGen.next()}>{title}</div>
            </div>
            <BoundDocElement content={content} idGen={idGen}/>
        </div>)

library.CustomComponent = ({componentName, componentProps, idGen}) => {
    const Component = library[componentName]
    if (!Component) {
        console.warn("can't find a custom component: ", componentName)
        return null
    } else {
        return <Component {...componentProps} idGen={idGen}/>
    }
}

library.GraphVizSvg = GraphVizSvg
library.GraphVizFlow = GraphVizFlow

library.Page = ({title, content}) => (<div className="page-content">
    <BoundDocElement key={title} content={content} idGen={new Id()}/>
</div>)

export default library
