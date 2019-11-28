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

import {isSimpleValueToken} from './codeUtils'
import {documentationNavigation} from '../structure/DocumentationNavigation'
import {isExternalUrl, onLocalUrlClick} from '../structure/links'

import 'prismjs/themes/prism-coy.css'

const SimpleCodeToken = ({token}) => {
    if (isSimpleValueToken(token)) {
        return <React.Fragment>{token}</React.Fragment>
    }

    const className = (token.type === 'text') ? '' : 'token ' + token.type
    return token.link ?
        renderLinkData(token, className):
        renderSpan(token, className)
}

function renderSpan(token, className) {
    return (
        <span className={className} onClick={token.onClick}>
            {renderData(token)}
        </span>
    )
}

function renderLinkData(token, className) {
    const isLocalNavigation = !isExternalUrl(token.link);

    const url = isLocalNavigation ?
        documentationNavigation.fullPageUrl(token.link):
        token.link

    const onClick = isLocalNavigation ? (e) => onLocalUrlClick(e, url) : null
    const targetProp = isLocalNavigation ? {} : {target: "_blank"}

    return (
        <a href={url} className={className} onClick={onClick} {...targetProp}>
            {renderData(token)}
        </a>
    )
}

function renderData(token) {
    if (isSimpleValueToken(token)) {
        return token
    }

    if (Array.isArray(token.content)) {
        return token.content.map((d, idx) => <SimpleCodeToken key={idx} token={d}/>)
    }

    if (typeof token === 'object') {
        return <SimpleCodeToken token={token.content} onClick={token.onClick}/>
    }

    return JSON.stringify(token)
}

export default SimpleCodeToken