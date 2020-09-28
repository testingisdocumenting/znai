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
import {imageAdditionalPreviewUrlParam} from './imagePreviewAdditionalUrlParam'

import './Image.css'

const Image = ({destination, inlined, isNarrow, timestamp}) => {
    const className = "znai-image" + (inlined ? " inlined" : "")
        + (isNarrow ? " content-block" : "")

    const alt = `image ${destination} was not found`

    return (
        <div className={className}>
            <img alt={alt} src={destination + imageAdditionalPreviewUrlParam(timestamp)}/>
        </div>
    )
}

export default Image
