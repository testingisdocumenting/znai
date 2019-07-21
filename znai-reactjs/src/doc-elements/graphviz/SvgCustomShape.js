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

import React, { Component } from 'react'

class SvgCustomShape extends Component {
    render() {
        const {cx, cy, svg, colors, style} = this.props
        const transform = `translate(${cx} ${cy})`
        const localStyle = colors ? {stroke: colors.line, fill: colors.fill} : {}
        const combinedStyle = {...style, ...localStyle}

        return <g transform={transform} style={combinedStyle} dangerouslySetInnerHTML={{__html: svg}}/>
    }
}

export default SvgCustomShape