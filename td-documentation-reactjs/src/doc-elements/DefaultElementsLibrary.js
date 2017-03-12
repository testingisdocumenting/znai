import React from 'react'
import DocElement from './default-elements/DocElement'
import Page from './default-elements/Page'
import Snippet from './default-elements/Snippet'
import GraphVizSvg from './graphviz/GraphVizSvg'
import GraphVizFlow from './graphviz/GraphVizFlow'
import SimpleTable from './table/SimpleTable'
import Tabs from './tabs/Tabs'
import EmbeddedAnnotatedImage from './images/EmbeddedAnnotatedImage'

const library = {}

const BoundDocElement = ({content}) => <DocElement content={content} elementsLibrary={library}/>

library.DocElement = BoundDocElement
library.Emphasis = ({content}) => (<span className="emphasis"><BoundDocElement content={content}/></span>)
library.StrongEmphasis = ({content}) => (<span className="strong-emphasis"><BoundDocElement content={content}/></span>)
library.Link = ({url, content}) => (<a href={url}><BoundDocElement content={content}/></a>)
library.Paragraph = ({content}) => <div className="paragraph content-block"><BoundDocElement content={content}/></div>
library.BlockQuote = ({content}) => <blockquote className="content-block"><BoundDocElement content={content}/></blockquote>
library.SimpleText = ({text}) => <span className="simple-text">{text}</span>
library.InlinedCode = ({code}) => <code>{code}</code>
library.SoftLineBreak = () => <span> </span>
library.HardLineBreak = () => <br />
library.ThematicBreak = () => <hr />

library.Image = ({destination, inlined}) => {
    const className = "image" + (inlined ? " inlined" : "")
    return (<div className={className}><img alt="not found" src={destination}/></div>)
}

library.Snippet = Snippet

library.BulletList = ({tight, bulletMarker, content}) => {
    const className = "content-block" + (tight ? " tight" : "")
    return (<ul className={className}><BoundDocElement content={content}/></ul>)
}

library.OrderedList = ({delimiter, startNumber, content}) => <ol className="content-block" start={startNumber}><BoundDocElement content={content}/></ol>

library.ListItem = ({content}) => <li><BoundDocElement content={content}/></li>

library.Section = ({id, title, content}) => {
    return (
        <div className="section" key={title}>
            <div className="content-block">
                <div className="section-title" id={id}>{title}</div>
            </div>
            <BoundDocElement content={content}/>
        </div>)
}

library.GraphVizDiagram = (props) => <div className="graphviz-diagram"><GraphVizSvg {...props}/></div>
library.GraphVizFlow = GraphVizFlow

library.SimpleTable = SimpleTable
library.EmbeddedAnnotatedImage = EmbeddedAnnotatedImage

library.Tabs = Tabs(library)
library.Page = Page(library)

library.CustomComponent = ({componentName, componentProps}) => {
    const Component = library[componentName]
    if (!Component) {
        console.warn("can't find a custom component: ", componentName)
        return null
    } else {
        return <Component {...componentProps}/>
    }
}

export default library
