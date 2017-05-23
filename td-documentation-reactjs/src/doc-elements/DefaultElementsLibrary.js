import React from 'react'
import DocElement from './default-elements/DocElement'
import {Page, presentationPageHandler} from './default-elements/Page'
import {Section, presentationSectionHandler} from './default-elements/Section'
import {BlockQuote, presentationBlockQuoteHandler} from './default-elements/BlockQuote'
import {Snippet, presentationSnippetHandler} from './default-elements/Snippet'
import {BulletList, presentationUnorderedListHandler} from './bullets/BulletList'
import GraphVizSvg from './graphviz/GraphVizSvg'
import DocumentationGraphVizFlow from './graphviz/DocumentationGraphVizFlow'
import Table from './table/Table'
import {Tabs, presentationTabsHandler} from './tabs/Tabs'
import {Columns, presentationColumnsHandler} from './columns/Columns'
import Json from './json/Json'
import presentationJson from './json/PresentationJson'
import LatexMath from './math/LatexMath'
import EmbeddedAnnotatedImage from './images/EmbeddedAnnotatedImage'
import presentationAnnotatedImageHandler from './images/PresentationAnnotatedImage'
import presentationGraphVizHandler from './graphviz/PresentationGraphVizFlow'

const library = {}
const presentationElementHandlers = {}

library.DocElement = DocElement
library.Emphasis = (props) => (<span className="emphasis"><props.elementsLibrary.DocElement {...props}/></span>)
library.StrongEmphasis = (props) => (<span className="strong-emphasis"><props.elementsLibrary.DocElement {...props}/></span>)
library.Link = ({url, ...props}) => (<a href={url}><props.elementsLibrary.DocElement {...props}/></a>)
library.Paragraph = (props) => <div className="paragraph content-block"><props.elementsLibrary.DocElement {...props}/></div>

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

library.OrderedList = ({delimiter, startNumber, ...props}) => <ol className="content-block" start={startNumber}>
    <props.elementsLibrary.DocElement {...props}/>
</ol>

library.ListItem = (props) => <li><props.elementsLibrary.DocElement {...props}/></li>

library.Section = Section
presentationElementHandlers.Section = presentationSectionHandler

library.GraphVizDiagram = (props) => <div className="graphviz-diagram"><GraphVizSvg {...props}/></div>

library.GraphVizFlow = DocumentationGraphVizFlow
presentationElementHandlers.GraphVizFlow = presentationGraphVizHandler

library.Table = Table
presentationElementHandlers.Table = {component: Table, numberOfSlides: () => 1}

library.LatexMath = LatexMath
presentationElementHandlers.LatexMath = {component: LatexMath, numberOfSlides: () => 1}

library.AnnotatedImage = EmbeddedAnnotatedImage
presentationElementHandlers.AnnotatedImage = presentationAnnotatedImageHandler

library.Tabs = Tabs
presentationElementHandlers.Tabs = presentationTabsHandler

library.Columns = Columns
presentationElementHandlers.Columns = presentationColumnsHandler

library.Json = Json
presentationElementHandlers.Json = presentationJson

library.Page = Page
presentationElementHandlers.Page = presentationPageHandler

export {library as elementsLibrary, presentationElementHandlers}
