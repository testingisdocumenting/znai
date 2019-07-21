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

import Icon from '../icons/Icon'

import './SectionTitle.css'

const SectionTitle = ({id, title}) => {
    return id ? (
        <h1 className="section-title" id={id}>{title}
            <a href={"#" + id}><Icon id="link"/></a>
        </h1>
    ) : (
        <h1 className="empty-section-title" id='implicit-section'/>
    )
}

export default SectionTitle
