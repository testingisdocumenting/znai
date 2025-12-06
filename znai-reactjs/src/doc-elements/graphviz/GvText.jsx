/*
 * Copyright 2025 znai maintainers
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
import {removeCustomProps} from './gvUtils'

import LinkWrap from './LinkWrap'

class GvText extends React.Component {
    render() {
        const {colors, isInversedTextColor, url} = this.props
        const cleanedUpProps = removeCustomProps(this.props)

        const fill = isInversedTextColor ? colors.inversedText : colors.text

        return (
            <LinkWrap url={url}>
                <text {...cleanedUpProps} fill={fill}>
                    {this.props.children[0]}
                </text>
            </LinkWrap>
        )
    }
}

export default GvText