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
import GraphVizSvg from './GraphVizSvg'
import {isAllAtOnce} from '../meta/meta'
/* eslint-disable react-refresh/only-export-components */
const PresentationGraphVizSvg = ({slideIdx, meta, idsToHighlight, ...props}) => {
    const idsToUse = (!idsToHighlight || isAllAtOnce(meta)) ?
        idsToHighlight:
        idsToHighlight.slice(0, slideIdx)

    return <GraphVizSvg idsToHighlight={idsToUse} {...props}/>
}

function numberOfSlides({_data, idsToHighlight, meta}) {
    return (!idsToHighlight || isAllAtOnce(meta)) ?
        1 :
        (idsToHighlight.length + 1)
}

export default {component: PresentationGraphVizSvg, numberOfSlides}
