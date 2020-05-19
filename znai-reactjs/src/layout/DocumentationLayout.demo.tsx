/*
 * Copyright 2020 znai maintainers
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

import React from 'react';

import { Registry } from 'react-component-viewer';
import { DocumentationLayout } from './DocumentationLayout';
import { DocMeta } from '../structure/docMeta';
import { testLongToc } from '../structure/toc/toc.test.data';
import { ViewPortProvider } from '../theme/ViewPortContext';

const dummy = <div/>;

const docMeta: DocMeta = {
  id: 'test',
  type: 'Guide',
  title: 'My Product',
  previewEnabled: true,
};

export function documentationLayoutDemo(registry: Registry) {
  registry.add('mobile', () => (
    <ViewPortProvider isMobileForced={true}>
      <div style={{width: 500}}>
        <DocumentationLayout searchPopup={dummy}
                             renderedPage={dummy}
                             renderedNextPrevNavigation={dummy}
                             renderedFooter={dummy}
                             docMeta={docMeta}
                             toc={testLongToc()}
                             selectedTocItem={undefined}
                             onHeaderClick={noOp}
                             onSearchClick={noOp}
                             onTocItemClick={noOp}
                             onTocItemPageSectionClick={noOp}
                             onNextPage={noOp}
                             onPrevPage={noOp}/>
      </div>
    </ViewPortProvider>
  ))
}

function noOp() {
}