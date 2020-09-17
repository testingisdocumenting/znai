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
import PresentationRegistry from './PresentationRegistry';

import { SlidePanel } from './SlidePanel';

import './PresenterMode.css';

interface Props {
  presentationRegistry: PresentationRegistry;
  slideIdx: number;
}

export function PresenterMode({presentationRegistry, slideIdx}: Props) {
  return (
    <div className="znai-presenter-mode">
      <div className="znai-presenter-current-next">
        <SlideContent label="next" presentationRegistry={presentationRegistry} slideIdx={slideIdx + 1}/>
        <SlideContent label="after next" presentationRegistry={presentationRegistry} slideIdx={slideIdx + 2}/>
      </div>
    </div>
  );
}

interface ContentProps extends Props {
  label: string;
}

function SlideContent({label, presentationRegistry, slideIdx}: ContentProps) {
  return (
    <div className="znai-presenter-slide">
      <div className="znai-presenter-slide-title">{label}</div>
      <SlidePanel presentationRegistry={presentationRegistry} currentSlideIdx={slideIdx}/>
    </div>
  )
}