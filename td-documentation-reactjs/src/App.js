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

const snippets = new Registry('snippets')
snippets.registerAsGrid('Json', 0, jsonDemo)
snippets.registerAsGrid('DocUtils', 0, docUtilsDemo)
snippets.registerAsGrid('Lang Class', 0, langClassDemo)
snippets.registerAsGrid('Lang Function', 0, langFunctionDemo)
snippets.registerAsGrid('API Parameters', 0, apiParametersDemo)
snippets.registerAsGrid('Open API', 0, openApiOperationDemo)
snippets.registerAsGrid('Open API Schema', 0, openApiSchemaDemo)

const visuals = new Registry('visuals')
visuals.registerAsTabs('Image Annotations', imageAnnotationDemo)

const layout = new Registry('layout')
layout.registerAsTabs('Pages', pagesDemo)
layout.registerAsGrid('Tabs', 300, tabsDemo)

const endToEnd = new Registry('end to end')
endToEnd.registerAsMiniApp('full documentation navigation', '/preview', documentationDemo)

class App extends Component {
    render() {
        return (
            <ComponentsViewer registries={[snippets, visuals, layout, endToEnd]}/>
        );
    }
}

export default App;
