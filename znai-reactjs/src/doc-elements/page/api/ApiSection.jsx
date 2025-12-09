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

import './ApiSection.css'

const ApiSection = ({elementsLibrary, id, title, isSelected, height, content}) => {
    const titleClassName = 'title-block' + (isSelected ? ' selected' : '')

    return (
        <React.Fragment>
            { }
            <a name={id} href={isSelected ? '' : ('#' + id)}>
                <div className={titleClassName}>
                    <CollapseIndicator isSelected={isSelected}/>
                    <div className="title">{title}</div>
                </div>
            </a>

            <div className="api-section-content" style={heightStyle()}>
                <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                            content={content}/>
            </div>
        </React.Fragment>
    )

    function heightStyle() {
        if (! height) {
            return {}
        }

        return isSelected ?
            {maxHeight: height}:
            {maxHeight: 0}
    }
}

function CollapseIndicator({isSelected}) {
    const className = 'collapse-indicator ' + (isSelected ? 'uncollapsed' : 'collapsed')

    return (
        <div className={className}/>
    )
}

export default ApiSection