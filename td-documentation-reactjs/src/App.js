import 'bootstrap/dist/css/bootstrap.css'
import './App.css'
import './doc-elements/DocumentationLayout.css'
import './doc-elements/search/Search.css'

import React, {Component} from 'react'

import {ComponentsViewer, Registry} from 'react-components-viewer'
import {tabsDemo} from './doc-elements/tabs/Tabs.demo'
import {jsonDemo} from './doc-elements/json/Json.demo'
import {langClassDemo} from './doc-elements/lang/LangClass.demo'
import {langFunctionDemo} from './doc-elements/lang/LangFunction.demo'
import {documentationDemo} from './doc-elements/Documentation.demo'
import {openApiOperationDemo} from './doc-elements/open-api/operation/OpenApiOperation.demo'
import {openApiSchemaDemo} from './doc-elements/open-api/schema/OpenApiSchema.demo'
import {docUtilsDemo} from './doc-elements/doc-utils/DocUtils.demo'
import {pagesDemo} from './doc-elements/page/Page.demo'
import {imageAnnotationDemo} from './doc-elements/images/EmbeddedAnnotatedImage.demo'
import {apiParametersDemo} from './doc-elements/api/ApiParameters.demo'
import {jsxDemo} from './doc-elements/jsx/Jsx.demo'
import {snippetsDemo} from './doc-elements/code-snippets/Snippet.demo'
import {documentationPreparationDemo} from './screens/documentation-preparation/DocumentationPreparation.demo'
import {setDocMeta} from './doc-elements/docMeta'
import {landingDemo} from './screens/landing/Landing.demo'
import {jupyterDemo} from './doc-elements/jupyter/Jupyter.demo'
import {tocPanelDemo} from './doc-elements/structure/TocPanel.demo'
import {xmlDemo} from './doc-elements/xml/Xml.demo'
import {xmlPresentationDemo} from './doc-elements/xml/PresentationXml.demo'
import {searchPopupDemo} from './doc-elements/search/Search.demo'
import {typographyDemo} from './doc-elements/typography/Typography.demo'
import {graphVizSvgDemo} from './doc-elements/graphviz/GraphVizSvg.demo'
import {graphVizSvgPresentationDemo} from './doc-elements/graphviz/PresentationGraphVizSvg.demo'

const docMeta = {
    id: 'preview',
    type: 'User Guide',
    title: 'MDoc',
    previewEnabled: true,
    hipchatRoom: 'Test Room',
    viewOn: {
        link: 'https://github.com/twosigma/TestingIsDocumenting/td-documentation-cli/documentation',
        title: 'View On GitHub'
    }
}

setDocMeta(docMeta)

const snippets = new Registry('snippets')
snippets.registerAsGrid('Code Snippet', 0, snippetsDemo)
snippets.registerAsGrid('Json', 0, jsonDemo)
snippets.registerAsGrid('Xml', 0, xmlDemo)
snippets.registerAsGrid('Xml Presentation', 0, xmlPresentationDemo)
snippets.registerAsGrid('Jsx', 0, jsxDemo)
snippets.registerAsGrid('DocUtils', 0, docUtilsDemo)
snippets.registerAsGrid('Lang Class', 0, langClassDemo)
snippets.registerAsGrid('Lang Function', 0, langFunctionDemo)
snippets.registerAsGrid('API Parameters', 0, apiParametersDemo)
snippets.registerAsGrid('Open API', 0, openApiOperationDemo)
snippets.registerAsGrid('Open API Schema', 0, openApiSchemaDemo)
snippets.registerAsGrid('Jupyter', 0, jupyterDemo)
snippets.registerAsGrid('GraphViz SVG', 0, graphVizSvgDemo)
snippets.registerAsTabs('GraphViz SVG Presentation', graphVizSvgPresentationDemo)

const visuals = new Registry('visuals')
visuals.registerAsTabs('Image Annotations', imageAnnotationDemo)

const layout = new Registry('layout')
layout.registerAsTabs('Pages', pagesDemo)
layout.registerAsGrid('Tabs', 300, tabsDemo)
layout.registerSingle('TOC', tocPanelDemo)
layout.registerAsGrid('Typography', 0, typographyDemo)

const screens = new Registry('screens')
screens.registerAsTabs('Documentation Preparation', documentationPreparationDemo)
screens.registerAsTabs('Landing', landingDemo)
screens.registerAsTabs('Search Popup', searchPopupDemo)

const endToEnd = new Registry('end to end')
endToEnd.registerAsMiniApp('full documentation navigation', '/preview', documentationDemo)

class App extends Component {
    render() {
        return (
            <ComponentsViewer registries={[snippets, visuals, layout, screens, endToEnd]}/>
        );
    }
}

export default App;
