/*
 * Copyright 2021 znai maintainers
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

import { Icon } from '../icons/Icon'

import {PresentationHeading} from './PresentationHeading'

import './SubHeading.css'
import { DocElement } from "./DocElement";

export function SubHeading({elementsLibrary, level, title, id, payload}) {
    const Element = `h${level}`

  const payloadContent = payload ?
    payload.map(e => e.payload) : undefined

  return (
        <Element className="content-block" id={id}>
            <span>{title}
              {payloadContent && <DocElement content={payloadContent} elementsLibrary={elementsLibrary}/>}
            </span>
            <a href={"#" + id}><Icon id="link"/></a>
        </Element>
    )
}

export const presentationSubHeading = {component: PresentationHeading, numberOfSlides: () => 1}