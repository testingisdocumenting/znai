/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'

import Theme from '../theme/Theme'
import {themeRegistry} from '../theme/ThemeRegistry'

import znaiDarkTheme from '../theme/znai-dark/znaiDarkTheme'

import { DocElement } from './default-elements/DocElement'
import {Page, presentationPageHandler} from './page/Page'
import PageTitle from './page/PageTitle'
import {SectionTitle} from './default-elements/SectionTitle'
import {presentationSectionHandler, Section} from './default-elements/Section'
import {BlockQuote, presentationBlockQuoteHandler} from './quote/BlockQuote'
import {presentationSnippetHandler, Snippet} from './code-snippets/Snippet'
import {InlinedCode} from './code-snippets/InlinedCode'
import {BulletList, presentationBulletListHandler} from './bullets/BulletList'
import CustomReactJSComponent from './custom/CustomReactJSComponent'
import Anchor from './default-elements/Anchor'
import Link from './default-elements/Link'
import {SubHeading, presentationSubHeading} from './default-elements/SubHeading'
import ListItem from './bullets/ListItem'
import { Paragraph, presentationParagraph } from './paragraph/Paragraph'
import { AttentionBlock, presentationAttentionBlock } from './paragraph/AttentionBlock'
import GraphVizSvg from './graphviz/GraphVizSvg'
import presentationGraphVizSvg from './graphviz/PresentationGraphVizSvg'
import DocumentationGraphVizFlow from './graphviz/DocumentationGraphVizFlow'
import Table from './table/Table'
import {presentationTabsHandler, Tabs} from './tabs/Tabs'
import {Columns, presentationColumnsHandler} from './columns/Columns'
import { Icon } from './icons/Icon'
import KeyboardShortcut from './keyboard/KeyboardShortcut'
import Json from './json/Json'
import presentationJson from './json/PresentationJson'
import Xml from './xml/Xml'
import presentationXml from './xml/PresentationXml'
import {presentationSvgHandler, Svg} from './svg/Svg'
import Latex from './latex/Latex'
import InlinedLatex from './latex/InlinedLatex'
import Mermaid from './mermaid/Mermaid'
import {EchartGeneric, presentationEchartHandler} from "./charts/EchartGeneric";
import Image from './images/Image'
import {CliCommand, presentationCliCommandHandler} from './cli/CliCommand'
import {CliOutput, presentationCliOutput} from './cli/CliOutput'
import presentationAnnotatedImageHandler from './images/PresentationAnnotatedImage'
import presentationGraphVizHandler from './graphviz/PresentationGraphVizFlow'
import {MarkdownAndResult, presentationMarkdownAndResultHandler} from './markdown/MarkdownAndResult'
import WebTauRest from './test-results/WebTauRest'
import LangClass from './lang/LangClass'
import LangFunction from './lang/LangFunction'
import ApiParameters, { presentationApiParameters } from "./api/ApiParameters";
import { Iframe, presentationIframe } from "./iframe/Iframe";
import JupyterCell from './jupyter/JupyterCell'
import {Spoiler} from './spoiler/Spoiler'
import {registerDocUtilsElements} from './doc-utils/DocUtils'
import JsxGroup from './jsx/JsxGroup'

import DiagramLegend from './diagrams/DiagramLegend'

import { DocumentationLayout } from '../layout/DocumentationLayout'
import Footer from '../structure/Footer'
import {Redirect} from '../structure/Redirect';
import {SimpleText} from './default-elements/SimpleText';
import { PageToc } from "./page/PageToc";
import { TextBadge } from "./badge/TextBadge";
import { DoxygenMember, presentationDoxygenMember } from "./doxygen/DoxygenMember";
import { AnnotatedImage } from "./images/AnnotatedImage";
import { AnnotatedImageWithOrderedList } from "./images/AnnotatedImageWithOrderedList";
import { OrderedList } from "./default-elements/OrderedList";
import { PythonMethod } from "./python/PythonMethod";
import { ApiLinkedTextBlock } from "./api/ApiLinkedTextBlock";
import { OpenApiMethodAndUrl } from "./open-api/OpenApiMethodAndUrl";
import { Card } from "./card/Card";
import { FootnoteReference } from "./footnote/FootnoteReference";
import { EmbeddedHtml } from "./html/EmbeddedHtml";
import { Asciinema } from "./asciinema/Asciinema";
import { ReadMore } from "./read-more/ReadMore.js";

const library = {}
const presentationElementHandlers = {}

library.DocElement = DocElement
library.Emphasis = (props) => (<span className="emphasis"><props.elementsLibrary.DocElement {...props}/></span>)
library.StrongEmphasis = (props) => (<span className="strong-emphasis"><props.elementsLibrary.DocElement {...props}/></span>)
library.StrikeThrough = (props) => (<del className="strike-through"><props.elementsLibrary.DocElement {...props}/></del>)
library.Link = Link
library.Anchor = Anchor

library.Paragraph = Paragraph
presentationElementHandlers.Paragraph = presentationParagraph

library.AttentionBlock = AttentionBlock
presentationElementHandlers.AttentionBlock = presentationAttentionBlock

library.SubHeading = SubHeading
presentationElementHandlers.SubHeading = presentationSubHeading

library.BlockQuote = BlockQuote
presentationElementHandlers.BlockQuote = presentationBlockQuoteHandler

library.SimpleText = SimpleText
library.InlinedCode = InlinedCode
library.SoftLineBreak = () => <span> </span>
library.HardLineBreak = () => <br />
library.ThematicBreak = () => <hr />

library.ApiLinkedTextBlock = ApiLinkedTextBlock;

library.Snippet = Snippet
presentationElementHandlers.Snippet = presentationSnippetHandler

library.CustomReactJSComponent = CustomReactJSComponent

library.EmptyBlock = () => (<div/>)

library.LangClass = wrappedInContentBlock(LangClass)
library.LangFunction = wrappedInContentBlock(LangFunction)

library.BulletList = BulletList
presentationElementHandlers.BulletList = presentationBulletListHandler

library.Icon = Icon
library.KeyboardShortcut = KeyboardShortcut

library.TextBadge = TextBadge

library.OrderedList = OrderedList

presentationElementHandlers.OrderedList = {component: library.OrderedList,
    numberOfSlides: () => 1}

library.ListItem = ListItem

library.SectionTitle = SectionTitle
library.Section = Section
presentationElementHandlers.Section = presentationSectionHandler

library.GraphVizDiagram = GraphVizSvg
presentationElementHandlers.GraphVizDiagram = presentationGraphVizSvg

library.GraphVizFlow = DocumentationGraphVizFlow
presentationElementHandlers.GraphVizFlow = presentationGraphVizHandler

library.Table = Table
presentationElementHandlers.Table = {component: Table, numberOfSlides: () => 1}

library.InlinedLatex = InlinedLatex

library.Latex = Latex
presentationElementHandlers.Latex = {component: Latex, numberOfSlides: () => 1}

library.Mermaid = Mermaid
presentationElementHandlers.Mermaid = {component: Mermaid, numberOfSlides: () => 1}

library.Image = Image
presentationElementHandlers.Image = {component: Image, numberOfSlides: () => 1}

library.AnnotatedImage = AnnotatedImage
presentationElementHandlers.AnnotatedImage = presentationAnnotatedImageHandler

library.AnnotatedImageWithOrderedList = AnnotatedImageWithOrderedList

library.Card = Card;

library.FootnoteReference = FootnoteReference;

library.Tabs = Tabs
presentationElementHandlers.Tabs = presentationTabsHandler

library.Columns = Columns
presentationElementHandlers.Columns = presentationColumnsHandler

library.Json = Json
presentationElementHandlers.Json = presentationJson

library.Xml = Xml
presentationElementHandlers.Xml = presentationXml

library.DoxygenMember = DoxygenMember
presentationElementHandlers.DoxygenMember = presentationDoxygenMember

library.PythonMethod = PythonMethod

library.JsxGroup = JsxGroup

library.Svg = Svg
presentationElementHandlers.Svg = presentationSvgHandler

library.Iframe = Iframe
presentationElementHandlers.Iframe = presentationIframe

library.Page = Page
presentationElementHandlers.Page = presentationPageHandler

library.PageTitle = PageTitle
library.PageToc = PageToc

library.MarkdownAndResult = MarkdownAndResult
presentationElementHandlers.MarkdownAndResult = presentationMarkdownAndResultHandler

library.EchartGeneric = EchartGeneric
presentationElementHandlers.EchartGeneric = presentationEchartHandler

library.CliCommand = CliCommand
presentationElementHandlers.CliCommand = presentationCliCommandHandler

library.CliOutput = CliOutput
presentationElementHandlers.CliOutput = presentationCliOutput

library.EmbeddedHtml = EmbeddedHtml

library.Spoiler = Spoiler
library.ReadMore = ReadMore

library.ApiParameters = ApiParameters
presentationElementHandlers.ApiParameters = presentationApiParameters

library.JupyterCell = JupyterCell

library.WebTauRest = WebTauRest

library.OpenApiMethodAndUrl = OpenApiMethodAndUrl
library.DiagramLegend = DiagramLegend

registerDocUtilsElements(library)

library.DocumentationLayout = DocumentationLayout
library.Footer = Footer

library.Redirect = Redirect
library.Asciinema = Asciinema

/**
 * to make a DocElement aligned with a page content it needs to have a content-block assigned.
 * We can't content-block to each component as we may need to used them to build other components.
 *
 * @param Component component to wrap
 */
function wrappedInContentBlock(Component) {
    return (props) => <div className="content-block"><Component {...props}/></div>
}

themeRegistry.registerAsBase(new Theme({
    name: 'default', themeClassName: '',
    elementsLibrary: library,
    presentationElementHandlers: presentationElementHandlers}))

themeRegistry.register(znaiDarkTheme)

export {library as elementsLibrary, presentationElementHandlers}
