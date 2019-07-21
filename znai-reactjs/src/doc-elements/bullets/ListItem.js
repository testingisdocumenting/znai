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
import {startsWithIcon, removeIcon, extractIconId} from './bulletUtils'

import './ListItem.css'

const ListItem = (props) => {
    let content = props.content
    const hasIcon = startsWithIcon(content)

    const className = "list-item" + (hasIcon ? " icon-based" : "")
    const childrenContent = hasIcon ? removeIcon(content) : content
    const children = <props.elementsLibrary.DocElement {...props} content={childrenContent}/>

    return <li className={className}>
        {hasIcon ? <Icon id={extractIconId(content)}/> : null}
        {children}
    </li>
}

export default ListItem
