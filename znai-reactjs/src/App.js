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
import {imageAnnotationDemo} from './doc-elements/images/EmbeddedAnnotatedImage.demo'
import {apiParametersDemo} from './doc-elements/api/ApiParameters.demo'
import {jsxDemo} from './doc-elements/jsx/Jsx.demo'
import {snippetsDemo, snippetsTwoSidesDemo} from './doc-elements/code-snippets/Snippet.demo'
import {inlinedCodeDemo} from './doc-elements/code-snippets/InlinedCode.demo'
import {documentationPreparationDemo} from './screens/documentation-preparation/DocumentationPreparation.demo'
import {setDocMeta} from './doc-elements/docMeta'
import {landingDemo} from './screens/landing/Landing.demo'
import {jupyterDemo} from './doc-elements/jupyter/Jupyter.demo'
import {tocPanelDemo} from './structure/toc/TocPanel.demo'
import {xmlDemo} from './doc-elements/xml/Xml.demo'
import {xmlPresentationDemo} from './doc-elements/xml/PresentationXml.demo'
import {searchPopupDemo} from './doc-elements/search/Search.demo'
import {blockQuoteDemo} from './doc-elements/quote/BlockQuote.demo'
import {blockQuotePresentationDemo} from './doc-elements/quote/PresentationBlockQuote.demo'
import {typographyDemo} from './doc-elements/typography/Typography.demo'
import {graphVizSvgDemo} from './doc-elements/graphviz/GraphVizSvg.demo'
import {graphVizSvgPresentationDemo} from './doc-elements/graphviz/PresentationGraphVizSvg.demo'
import {cliCommandDemo} from './doc-elements/cli/CliCommand.demo'
import {yamlSnippetDemo} from './doc-elements/code-snippets/Yaml.demo'
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
import testData from "./doc-elements/TestData"
import {subHeadingPresentationDemo} from './doc-elements/default-elements/PresentationSubHeading.demo'
import {documentationTracking} from './doc-elements/tracking/DocumentationTracking'
import {updateGlobalDocReferences} from './doc-elements/references/globalDocReferences'
import {paragraphDemo} from './doc-elements/default-elements/Paragraph.demo'
import {defaultNextPrevNavigationDemo} from './doc-elements/page/default/PageDefaultNextPrevNavigation.demo'
import {twoSidesNextPrevNavigationDemo} from './doc-elements/page/two-sides/TwoSidesNextPrevNavigation.demo'
import {bulletListsDemo} from './doc-elements/bullets/BulletList.demo'

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
    .registerAsGrid('Inlined Code', 0, inlinedCodeDemo)
    .registerAsGrid('Code Snippet In Two Sides Mode', 0, snippetsTwoSidesDemo)
    .registerAsGrid('Yaml Code Snippet', 0, yamlSnippetDemo)
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

registries.add('visuals')
    .registerAsGrid('Spoilers', 0, spoilerDemo)
    .registerAsGrid('Charts', 0, chartDemo)
    .registerAsTabs('Image Annotations', imageAnnotationDemo)
    .registerAsGrid('Embedded SVG', 0, embeddedSvgDemo)
    .registerAsGrid('SVG', 0, svgDemo)
    .registerAsGrid('GraphViz SVG', 0, graphVizSvgDemo)
    .registerAsRows('Diagram Legend', diagramLegendDemo)
    .registerAsGrid('Keyboard shortcuts', 0, keyboardShortcutsDemo)

registries.add('layout')
    .registerAsTabs('Pages', pagesDemo)
    .registerAsGrid('Tabs', 0, tabsDemo)
    .registerAsGrid('Tables', 0, tableDemo)
    .registerAsTabs('TOC', tocPanelDemo)
    .registerAsRows('Next/Prev navigation', defaultNextPrevNavigationDemo)
    .registerAsRows('Two Sides Next/Prev navigation', twoSidesNextPrevNavigationDemo)

registries.add('presentation')
    .registerAsTabs('Layout', presentationDemo)
    .registerAsTabs('SubHeading', subHeadingPresentationDemo)
    .registerAsTabs('Xml Presentation', xmlPresentationDemo)
    .registerAsTabs('SVG', svgPresentationDemo)
    .registerAsTabs('GraphViz SVG', graphVizSvgPresentationDemo)
    .registerAsTabs('Blockquote', blockQuotePresentationDemo)

registries.add('preview')
    .registerAsRows('Diff Highlight', diffTrackingDemo)
    .registerAsTabs('Page Gen Error', pageGenErrorDemo)

registries.add('screens')
    .registerAsTabs('Documentation Preparation', documentationPreparationDemo)
    .registerAsTabs('Landing', landingDemo)
    .registerAsTabs('Search Popup', searchPopupDemo)

registries.add('end to end')
    .registerAsMiniApp('full documentation navigation', /\/preview/,
        {'root': '/preview'},
        () => <Documentation {...testData.documentation}/>)

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
