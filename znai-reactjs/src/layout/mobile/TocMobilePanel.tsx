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

import TocMenu from '../TocMenu';
import React from 'react';
import { TocItem } from '../../structure/TocItem';

import { DarkLightThemeSwitcher } from '../DarkLightThemeSwitcher';

import './TocMobilePanel.css';

interface Props {
  toc: Partial<TocItem>[];
  selectedItem?: TocItem;

  onTocItemClick(dirName: string, fileName: string): void;

  onTocItemPageSectionClick(sectionId: string): void;
}

export function TocMobilePanel({toc, selectedItem, onTocItemClick, onTocItemPageSectionClick}: Props) {
  return (
    <div className="znai-toc-mobile-panel">
      <DarkLightThemeSwitcher/>
      <TocMenu toc={toc}
               selected={selectedItem}
               onTocItemPageSectionClick={onTocItemPageSectionClick}
               onTocItemClick={onTocItemClick}/>
    </div>
  )
}