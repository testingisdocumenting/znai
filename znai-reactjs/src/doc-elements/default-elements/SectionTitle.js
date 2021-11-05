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

import React from 'react'

import { Icon } from '../icons/Icon'
import {presentationModeListeners} from "../presentation/PresentationModeListener";

import './SectionTitle.css'
import { DocElement } from "./DocElement";

const SectionTitle = ({elementsLibrary, id, title, payload}) => {
    const payloadContent = payload ?
      payload.map(e => e.payload) : undefined

    return id ? (
        <h1 className="section-title" id={id}>{title}
            {payloadContent && <DocElement content={payloadContent} elementsLibrary={elementsLibrary}/>}
            <div className="znai-section-title-actions">
                <a href={"#" + id}><Icon id="link"/></a>
                <Icon id="maximize" className="znai-section-title-presentation" onClick={openPresentation}/>
            </div>
        </h1>
    ) : (
        // eslint-disable-next-line
        <h1 className="empty-section-title" id='implicit-section'/>
    )

    function openPresentation() {
        presentationModeListeners.notifyPresentationEnter(id)
    }
}

export default SectionTitle
