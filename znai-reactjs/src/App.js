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

import './App.css'
import './layout/DocumentationLayout.css'
import './doc-elements/search/Search.css'

import React, {Component} from 'react'

import {ComponentViewer, DropDowns, Registries} from 'react-component-viewer'
import {tabsDemo} from './doc-elements/tabs/Tabs.demo'
import {jsonDemo} from './doc-elements/json/Json.demo'
import {langClassDemo} from './doc-elements/lang/LangClass.demo'
import {langFunctionDemo} from './doc-elements/lang/LangFunction.demo'
import {openApiOperationDemo} from './doc-elements/open-api/operation/OpenApiOperation.demo'
import {openApiSchemaDemo} from './doc-elements/open-api/schema/OpenApiSchema.demo'
import {docUtilsDemo} from './doc-elements/doc-utils/DocUtils.demo'
import {pagesDemo} from './doc-elements/page/Page.demo'
import {imageDemo} from './doc-elements/images/Image.demo'
import {apiParametersDemo} from './doc-elements/api/ApiParameters.demo'
import {jsxDemo} from './doc-elements/jsx/Jsx.demo'
import {snippetsDemo, snippetsTwoSidesDemo} from './doc-elements/code-snippets/Snippet.demo'
import {inlinedCodeDemo} from './doc-elements/code-snippets/InlinedCode.demo'
import {documentationPreparationDemo} from './screens/documentation-preparation/DocumentationPreparation.demo'
import {setDocMeta} from './structure/docMeta'
import {landingDemo} from './screens/landing/Landing.demo'
import {jupyterDemo} from './doc-elements/jupyter/Jupyter.demo'
import {tocPanelDemo} from './layout/TocPanel.demo'
import {xmlDemo} from './doc-elements/xml/Xml.demo'
import {xmlPresentationDemo} from './doc-elements/xml/PresentationXml.demo'
import {searchPopupDemo} from './doc-elements/search/Search.demo'
import {blockQuoteDemo} from './doc-elements/quote/BlockQuote.demo'
import {blockQuotePresentationDemo} from './doc-elements/quote/PresentationBlockQuote.demo'
import {typographyDemo} from './doc-elements/typography/Typography.demo'
import {graphVizSvgDemo} from './doc-elements/graphviz/GraphVizSvg.demo'
import {graphVizSvgPresentationDemo} from './doc-elements/graphviz/PresentationGraphVizSvg.demo'
import {cliCommandDemo} from './doc-elements/cli/CliCommand.demo'
import {syntaxHighlightSnippetDemo} from './doc-elements/code-snippets/CodeSnippetSyntaxHighlight.demo'
import {latexDemo} from './doc-elements/latex/Latex.demo'
import {chartDemo} from './doc-elements/charts/Chart.demo'
import {tableDemo} from './doc-elements/table/Table.demo'
import {diagramLegendDemo} from "./doc-elements/diagrams/DiagramLegend.demo"
import {keyboardShortcutsDemo} from "./doc-elements/keyboard/KeyboardShortcut.demo"
import {svgDemo} from './doc-elements/svg/Svg.demo'
import {svgPresentationDemo} from './doc-elements/svg/PresentationSvg.demo'
import {embeddedSvgDemo} from './doc-elements/svg/EmbeddedSvg.demo'
import {presentationDemo} from './doc-elements/presentation/Presentation.demo'
import {spoilerDemo} from './doc-elements/spoiler/Spoiler.demo'
import {pageGenErrorDemo} from './doc-elements/page-gen-error/PageGenError.demo'
import {diffTrackingDemo} from './diff/DiffTracking.demo'

import {Documentation} from "./doc-elements/Documentation"
import {testDocumentation} from "./doc-elements/testDocumentation"
import {subHeadingPresentationDemo} from './doc-elements/default-elements/PresentationSubHeading.demo'
import {documentationTracking} from './doc-elements/tracking/DocumentationTracking'
import {updateGlobalDocReferences} from './doc-elements/references/globalDocReferences'
import {paragraphDemo} from './doc-elements/paragraph/Paragraph.demo'
import {defaultNextPrevNavigationDemo} from './doc-elements/page/default/PageDefaultNextPrevNavigation.demo'
import {twoSidesNextPrevNavigationDemo} from './doc-elements/page/two-sides/TwoSidesNextPrevNavigation.demo'
import {bulletListsDemo} from './doc-elements/bullets/BulletList.demo'
import {columnsDemo} from "./doc-elements/columns/Columns.demo";
import {tocMobileHeaderDemo} from "./layout/mobile/TocMobileHeader.demo";
import {documentationLayoutDemo} from "./layout/DocumentationLayout.demo";
import {cliOutputDemo} from "./doc-elements/cli/CliOutput.demo";
import {cliOutputPresentationDemo} from "./doc-elements/cli/PresentationCliOutput.demo";
import {bulletListPresentationDemo} from "./doc-elements/bullets/PresentationBulletList.demo";
import {paragraphPresentationDemo} from "./doc-elements/paragraph/PresentationParagraph.demo";
import {snippetPresentationDemo} from "./doc-elements/code-snippets/PresentationSnippet.demo";
import {snippetWithScrollPresentationDemo} from "./doc-elements/code-snippets/PresentationSnippetWithScroll.demo";
import {presentationStickySlidesDemo} from "./doc-elements/presentation/PresentationStickySlides.demo";
import {snippetsWithInlineCommentsDemo} from "./doc-elements/code-snippets/CodeSnippetWithInlineComments.demo";
import {notAuthorizedDemo} from "./screens/not-authorized/NotAuthorizedScreen.demo";
import {iframeDemo} from './doc-elements/iframe/Iframe.demo';

const docMeta = {
    id: 'preview',
    type: 'User Guide',
    title: 'Znai',
    previewEnabled: true,
    hipchatRoom: 'Test Room',
    viewOn: {
        link: 'https://github.com/testingisdocumenting/znai/znai-cli/documentation',
        title: 'View On GitHub'
    }
}

setDocMeta(docMeta)
updateGlobalDocReferences({
    'package.SuperClass': {
        pageUrl: '#super-url'
    }
})

const registries = new Registries()

registries.add('text')
    .registerAsGrid('Typography', 0, typographyDemo)
    .registerAsRows('Blockquote', blockQuoteDemo)
    .registerAsRows('Paragraph', paragraphDemo)
    .registerAsRows('Bullets', bulletListsDemo)

registries.add('snippets')
    .registerAsGrid('Code Snippet', 0, snippetsDemo)
    .registerAsGrid('Code Snippet With Bullets', 0, snippetsWithInlineCommentsDemo)
    .registerAsGrid('Code Snippet Syntax Highlight ', 0, syntaxHighlightSnippetDemo)
    .registerAsGrid('Code Snippet In Two Sides Mode', 0, snippetsTwoSidesDemo)
    .registerAsGrid('Inlined Code', 0, inlinedCodeDemo)
    .registerAsGrid('Json', 0, jsonDemo)
    .registerAsGrid('Xml', 0, xmlDemo)
    .registerAsGrid('Latex', 0, latexDemo)
    .registerAsGrid('Jsx', 0, jsxDemo)
    .registerAsGrid('DocUtils', 0, docUtilsDemo)
    .registerAsGrid('Lang Class', 0, langClassDemo)
    .registerAsGrid('Lang Function', 0, langFunctionDemo)
    .registerAsGrid('API Parameters', 0, apiParametersDemo)
    .registerAsGrid('Open API', 0, openApiOperationDemo)
    .registerAsGrid('Open API Schema', 0, openApiSchemaDemo)
    .registerAsGrid('Jupyter', 0, jupyterDemo)
    .registerAsGrid('CLI Command', 0, cliCommandDemo)
    .registerAsGrid('CLI Output', 0, cliOutputDemo)

registries.add('visuals')
    .registerAsGrid('Spoilers', 0, spoilerDemo)
    .registerAsGrid('Charts', 0, chartDemo)
    .registerAsRows('Image', imageDemo)
    .registerAsGrid('Embedded SVG', 0, embeddedSvgDemo)
    .registerAsGrid('SVG', 0, svgDemo)
    .registerAsGrid('GraphViz SVG', 0, graphVizSvgDemo)
    .registerAsRows('Diagram Legend', diagramLegendDemo)
    .registerAsGrid('Keyboard shortcuts', 0, keyboardShortcutsDemo)
    .registerAsGrid('Iframe', 0, iframeDemo)

registries.add('layout')
    .registerAsTabs('Pages', pagesDemo)
    .registerAsGrid('Tabs', 0, tabsDemo)
    .registerAsGrid('Tables', 0, tableDemo)
    .registerAsGrid('Columns', 0, columnsDemo)
    .registerAsTabs('TOC', tocPanelDemo)
    .registerAsTabs('Mobile Header', tocMobileHeaderDemo)
    .registerAsTabs('Layout', documentationLayoutDemo)
    .registerAsRows('Next/Prev navigation', defaultNextPrevNavigationDemo)
    .registerAsRows('Two Sides Next/Prev navigation', twoSidesNextPrevNavigationDemo)

registries.add('presentation')
    .registerAsTabs('Layout', presentationDemo)
    .registerAsTabs('Sticky Slides', presentationStickySlidesDemo)
    .registerAsTabs('SubHeading', subHeadingPresentationDemo)
    .registerAsTabs('Xml Presentation', xmlPresentationDemo)
    .registerAsTabs('SVG', svgPresentationDemo)
    .registerAsTabs('GraphViz SVG', graphVizSvgPresentationDemo)
    .registerAsTabs('Blockquote', blockQuotePresentationDemo)
    .registerAsTabs('Bullet Points', bulletListPresentationDemo)
    .registerAsTabs('Paragraph', paragraphPresentationDemo)
    .registerAsTabs('Code Snippets', snippetPresentationDemo)
    .registerAsTabs('Code Snippets With Scroll', snippetWithScrollPresentationDemo)
    .registerAsTabs('Cli Output', cliOutputPresentationDemo)

registries.add('preview')
    .registerAsRows('Diff Highlight', diffTrackingDemo)
    .registerAsTabs('Page Gen Error', pageGenErrorDemo)

registries.add('screens')
    .registerAsTabs('Documentation Preparation', documentationPreparationDemo)
    .registerAsTabs('Landing', landingDemo)
    .registerAsTabs('Not Authorized', notAuthorizedDemo)
    .registerAsTabs('Search Popup', searchPopupDemo)

registries.add('end to end')
    .registerAsMiniApp('full documentation navigation', /\/preview/,
        {'root': '/preview'},
        () => <Documentation {...testDocumentation}/>)

const dropDowns = new DropDowns()
dropDowns.add('Theme')
    .addItem('Default', 'Alt 1')
    .addItem('Dark', 'Alt 2')
    .onSelect(selectTheme)

const documentationTracker = {
    onPageOpen(pageId) {
        console.log('onPageOpen', pageId)
    },
    onLinkClick(currentPageId, url) {
        console.log('onLinkClick', currentPageId, url)
    },
    onNextPage(currentPageId) {
        console.log('onNextPage', currentPageId)
    },
    onPrevPage(currentPageId) {
        console.log('onPrevPage', currentPageId)
    },
    onScrollToSection(currentPageId, sectionIdTitle) {
        console.log('onScrollToSection', currentPageId, sectionIdTitle)
    },
    onTocItemSelect(currentPageId, tocItem) {
        console.log('onTocItemSelect', currentPageId, tocItem)
    },
    onSearchResultSelect(currentPageId, query, selectedPageId) {
        console.log('onSearchResultSelect', currentPageId, query, selectedPageId)
    },
    onInteraction(currentPageId, type, id) {
        console.log('onInteraction', currentPageId, type, id)
    },
    onPresentationOpen(currentPageId) {
        console.log('onPresentationOpen', currentPageId)
    }
}

documentationTracking.addListener(documentationTracker)

export class App extends Component {
    render() {
        return (
            <ComponentViewer registries={registries} dropDowns={dropDowns}/>
        )
    }
}

function selectTheme(label) {
    const name = label === 'Default' ? 'default' : 'znai-dark'
    window.znaiTheme.setExplicitly(name)
}
