import React from 'react'
import DocElement from './default-elements/DocElement'
import Page from './default-elements/Page'
import {Section, presentationSectionHandler} from './default-elements/Section'
import {BlockQuote, presentationBlockQuoteHandler} from './default-elements/BlockQuote'
import {Snippet, presentationSnippetHandler} from './default-elements/Snippet'
import {BulletList, presentationUnorderedListHandler} from './default-elements/BulletList'
import GraphVizSvg from './graphviz/GraphVizSvg'
import GraphVizFlow from './graphviz/GraphVizFlow'
import SimpleTable from './table/SimpleTable'
import Tabs from './tabs/Tabs'
import EmbeddedAnnotatedImage from './images/EmbeddedAnnotatedImage'
import presentationAnnotatedImageHandler from './images/PresentationAnnotatedImage'

const library = {}
const presentationElementHandlers = {}

const BoundDocElement = ({content}) => <DocElement content={content} elementsLibrary={library}/>

library.DocElement = BoundDocElement
library.Emphasis = ({content}) => (<span className="emphasis"><BoundDocElement content={content}/></span>)
library.StrongEmphasis = ({content}) => (<span className="strong-emphasis"><BoundDocElement content={content}/></span>)
library.Link = ({url, content}) => (<a href={url}><BoundDocElement content={content}/></a>)
library.Paragraph = ({content}) => <div className="paragraph content-block"><BoundDocElement content={content}/></div>

library.BlockQuote = BlockQuote
presentationElementHandlers.BlockQuote = presentationBlockQuoteHandler

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
presentationElementHandlers.Snippet = presentationSnippetHandler

library.BulletList = BulletList
presentationElementHandlers.BulletList = presentationUnorderedListHandler

library.OrderedList = ({delimiter, startNumber, content}) => <ol className="content-block" start={startNumber}><BoundDocElement content={content}/></ol>

library.ListItem = ({content}) => <li><BoundDocElement content={content}/></li>

library.Section = Section(library)
presentationElementHandlers.Section = presentationSectionHandler

library.GraphVizDiagram = (props) => <div className="graphviz-diagram"><GraphVizSvg {...props}/></div>
library.GraphVizFlow = GraphVizFlow

library.SimpleTable = SimpleTable

library.AnnotatedImage = EmbeddedAnnotatedImage
presentationElementHandlers.AnnotatedImage = presentationAnnotatedImageHandler

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

export {library as elementsLibrary, presentationElementHandlers}
