import React from 'react'
import DocElement from './default-elements/DocElement'
import {Page, presentationPageHandler} from './page/Page'
import SectionTitle from './default-elements/SectionTitle'
import {Section, presentationSectionHandler} from './default-elements/Section'
import {BlockQuote, presentationBlockQuoteHandler} from './default-elements/BlockQuote'
import {Snippet, presentationSnippetHandler} from './default-elements/Snippet'
import {BulletList, presentationBulletListHandler} from './bullets/BulletList'
import CustomReactJSComponent from './custom/CustomReactJSComponent'
import Anchor from './default-elements/Anchor'
import Link from './default-elements/Link'
import SubHeading from './default-elements/SubHeading'
import ListItem from './bullets/ListItem'
import Paragraph from './default-elements/Paragraph'
import GraphVizSvg from './graphviz/GraphVizSvg'
import DocumentationGraphVizFlow from './graphviz/DocumentationGraphVizFlow'
import Table from './table/Table'
import {Tabs, presentationTabsHandler} from './tabs/Tabs'
import {Columns, presentationColumnsHandler} from './columns/Columns'
import Icon from './icons/Icon'
import Json from './json/Json'
import presentationJson from './json/PresentationJson'
import {Svg, presentationSvgHandler} from './svg/Svg'
import LatexMath from './math/LatexMath'
import {Chart, presentationChartHandler} from './charts/Chart'
import Image from './images/Image'
import {CliCommand, presentationCliCommandHandler} from './cli/CliCommand'
import {CliOutput, presentationCliOutput} from './cli/CliOutput'
import EmbeddedAnnotatedImage from './images/EmbeddedAnnotatedImage'
import Footer from './structure/Footer'
import presentationAnnotatedImageHandler from './images/PresentationAnnotatedImage'
import presentationGraphVizHandler from './graphviz/PresentationGraphVizFlow'
import {MarkdownAndResult, presentationMarkdownAndResultHandler} from './markdown/MarkdownAndResult'
import WebTauRest from './test-results/WebTauRest'
import LangClass from './lang/LangClass'
import LangFunction from './lang/LangFunction'
import ApiParameters from './api/ApiParameters'
import OpenApiOperation from './open-api/operation/OpenApiOperation'
import {registerDocUtilsElements} from './doc-utils/DocUtils'
import JsxGroup from './jsx/JsxGroup'

const library = {}
const presentationElementHandlers = {}

library.DocElement = DocElement
library.Emphasis = (props) => (<span className="emphasis"><props.elementsLibrary.DocElement {...props}/></span>)
library.StrongEmphasis = (props) => (<span className="strong-emphasis"><props.elementsLibrary.DocElement {...props}/></span>)
library.StrikeThrough = (props) => (<del className="strike-through"><props.elementsLibrary.DocElement {...props}/></del>)
library.Link = Link
library.Anchor = Anchor
library.Paragraph = Paragraph

library.SubHeading = SubHeading
presentationElementHandlers.SubHeading = {component: SubHeading, numberOfSlides: () => 1}

library.BlockQuote = BlockQuote
presentationElementHandlers.BlockQuote = presentationBlockQuoteHandler

library.SimpleText = ({text}) => <span className="simple-text">{text}</span>
library.InlinedCode = ({code}) => <code>{code}</code>
library.SoftLineBreak = () => <span> </span>
library.HardLineBreak = () => <br />
library.ThematicBreak = () => <hr />

library.Snippet = Snippet
presentationElementHandlers.Snippet = presentationSnippetHandler

library.CustomReactJSComponent = CustomReactJSComponent

library.LangClass = wrappedInContentBlock(LangClass)
library.LangFunction = wrappedInContentBlock(LangFunction)

library.BulletList = BulletList
presentationElementHandlers.BulletList = presentationBulletListHandler

library.Icon = Icon

library.OrderedList = ({delimiter, startNumber, ...props}) => <ol className="content-block" start={startNumber}>
    <props.elementsLibrary.DocElement {...props}/>
</ol>

presentationElementHandlers.OrderedList = {component: library.OrderedList,
    numberOfSlides: () => 1}

library.ListItem = ListItem

library.SectionTitle = SectionTitle
library.Section = Section
presentationElementHandlers.Section = presentationSectionHandler

library.GraphVizDiagram = GraphVizSvg

library.GraphVizFlow = DocumentationGraphVizFlow
presentationElementHandlers.GraphVizFlow = presentationGraphVizHandler

library.Table = Table
presentationElementHandlers.Table = {component: Table, numberOfSlides: () => 1}

library.LatexMath = LatexMath
presentationElementHandlers.LatexMath = {component: LatexMath, numberOfSlides: () => 1}

library.Image = Image
presentationElementHandlers.Image = {component: Image, numberOfSlides: () => 1}

library.AnnotatedImage = EmbeddedAnnotatedImage
presentationElementHandlers.AnnotatedImage = presentationAnnotatedImageHandler

library.Tabs = Tabs
presentationElementHandlers.Tabs = presentationTabsHandler

library.Columns = Columns
presentationElementHandlers.Columns = presentationColumnsHandler

library.Json = Json
presentationElementHandlers.Json = presentationJson

library.JsxGroup = JsxGroup

library.Svg = Svg
presentationElementHandlers.Svg = presentationSvgHandler

library.Page = Page
presentationElementHandlers.Page = presentationPageHandler

library.MarkdownAndResult = MarkdownAndResult
presentationElementHandlers.MarkdownAndResult = presentationMarkdownAndResultHandler

library.Chart = Chart
presentationElementHandlers.Chart = presentationChartHandler

library.CliCommand = CliCommand
presentationElementHandlers.CliCommand = presentationCliCommandHandler

library.CliOutput = CliOutput
presentationElementHandlers.CliOutput = presentationCliOutput

library.ApiParameters = wrappedInContentBlock(ApiParameters)

library.Footer = Footer

library.WebTauRest = WebTauRest

library.OpenApiOperation = OpenApiOperation

registerDocUtilsElements(library)

/**
 * to make a DocElement aligned with a page content it needs to have a content-block assigned.
 * We can't content-block to each component as we may need to used them to build other components.
 *
 * @param Component component to wrap
 */
function wrappedInContentBlock(Component) {
    return (props) => <div className="content-block"><Component {...props}/></div>
}

export {library as elementsLibrary, presentationElementHandlers}
