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
/* eslint-disable react-refresh/only-export-components */

import React from 'react'

import './BlockQuote.css'

const BlockQuote = (props) => (
    <blockquote className="content-block">
        <props.elementsLibrary.DocElement {...props}/>
    </blockquote>
)

const PresentationBlockQuote = (props) => {
    const className = props.isPresentationDisplayed ? "no-animation" : "animate";
    return (
        <blockquote className={className}>
            <props.elementsLibrary.DocElement {...props}/>
        </blockquote>
    )
}

const presentationBlockQuoteHandler = {
    component: PresentationBlockQuote,
    numberOfSlides: () => 1
}

export {BlockQuote, presentationBlockQuoteHandler}
