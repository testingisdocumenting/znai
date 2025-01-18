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

import React, {Component} from 'react'

import {extractIconIds, extractTextLinesEmphasisOrFull} from '../bulletUtils'
import {isAllAtOnce} from '../../meta/meta'

import {Icon} from "../../icons/Icon";

import './HorizontalStripes.css'

const Stripe = ({iconId, text}) => {
    return (
        <div className="znai-horizontal-stripe">
            {iconId && <div className="znai-horizontal-stripe-icon"><Icon id={iconId}/></div>}
            <div className="znai-horizontal-stripe-text">{text}</div>
        </div>
    )
}

const EmptyStripe = () => {
    return <div className="znai-empty-horizontal-stripe"/>
}

class HorizontalStripes extends Component {
    render() {
        const {elementsLibrary, meta, content, slideIdx} = this.props
        const textLines = extractTextLinesEmphasisOrFull(content)
        const iconIds = extractIconIds(content)

        return (
            <div className="znai-horizontal-stripes">{content.map((item, idx) => {
                const Component = idx <= slideIdx || isAllAtOnce(meta) ? Stripe : EmptyStripe
                return <Component key={idx}
                                  {...this.props}
                                  iconId={iconIds[idx]}
                                  elementsLibrary={elementsLibrary}
                                  text={textLines[idx]}/>
            })}
            </div>
        )
    }

    static get isPresentationFullScreen() {
        return true
    }
}

export default HorizontalStripes
