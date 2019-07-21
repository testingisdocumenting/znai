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

import React, {Component} from 'react'

import {extractTextLinesEmphasisOrFull} from '../bulletUtils'
import {isAllAtOnce} from '../../meta/meta'

import './HorizontalStripes.css'

const Stripe = ({text}) => {
    return <div className="horizontal-stripe">{text}</div>
}

const EmptyStripe = () => {
    return <div className="empty-horizontal-stripe"/>
}

class HorizontalStripes extends Component {
    render() {
        const {elementsLibrary, meta, content, slideIdx} = this.props
        const textLines = extractTextLinesEmphasisOrFull(content)

        return (
            <div className="horizontal-stripes">{content.map((item, idx) => {
                const Component = idx <= slideIdx || isAllAtOnce(meta) ? Stripe : EmptyStripe
                return <Component key={idx}
                                  {...this.props}
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
