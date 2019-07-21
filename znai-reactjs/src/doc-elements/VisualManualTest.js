/*
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

// import Demo from './presentation/SvgPresentationDemo'
// import Demo from './default-elements/ParagraphDemo'
// import Demo from './svg/SvgDemo'
// import Demo from './presentation/PresentationDemo'
// import Demo from './DocumentationDemo'
// import Demo from './charts/ChartDemo'
// import Demo from './bullets/IconsAsBulletsDemo'
// import Demo from './bullets/BulletListDemo'
// import Demo from './cli/CliCommandDemo'
// import Demo from './columns/ColumnsDemo'
// import Demo from './lang/LangClassDemo'

// import Demo from './images/EmbeddedAnnotatedImageDemo'
// import Demo from './images/AnnotatedImageEditorDemo'
import Demo from './tabs/Tabs.demo'
// import Demo from './DiagramSlidesDemo'
// import Demo from './table/TableDemo'
// import Demo from './code-snippets/SnippetDemo'
// import Demo from './search/SearchDemo'

// import Demo from './json/JsonDemo'

// import Demo from './test-results/WebTauRestDemo'


import {setDocMeta} from './docMeta'

const docMeta = {
    id: 'preview',
    type: 'User Guide',
    title: 'MDoc',
    previewEnabled: true,
    hipchatRoom: 'Test Room',
    viewOn: {
        link: 'https://github.com/twosigma/TestingIsDocumenting/znai-cli/documentation',
        title: 'View On GitHub'
    }
}

setDocMeta(docMeta)

const VisualManualTest = () => (
    <Demo/>
)


export default VisualManualTest
