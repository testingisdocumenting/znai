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

import { Registry, simpleAction } from 'react-component-viewer';
import { TocMobileHeader } from './TocMobileHeader';
import { DocMeta } from '../../structure/docMeta';

const headerClick = simpleAction('header clicked');
const menuClick = simpleAction('menu clicked');

const docMeta: DocMeta = {
  id: 'test',
  type: 'Guide',
  title: 'My Product',
  previewEnabled: true,
};

export function tocMobileHeaderDemo(registry: Registry) {
  registry.add('default', () => (
    <div style={{width: 600}}>
      <TocMobileHeader docMeta={docMeta}
                       onHeaderClick={headerClick}
                       onMenuClick={menuClick}/>
    </div>))
}