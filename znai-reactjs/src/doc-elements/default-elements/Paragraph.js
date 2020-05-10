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

import {paragraphStartsWith, removeSuffixFromParagraph} from './paragraphUtils'

import './Paragraph.css'

const noteSuffix = "Note:"
const warningSuffix = "Warning:"
const questionSuffix = "Question:"
const avoidSuffix = "Avoid:"
const dontSuffix = "Don't:"
const doNotSuffix = "Do not:"

const DefaultParagraph = (props) => {
    return <div className="paragraph content-block"><props.elementsLibrary.DocElement {...props}/></div>
}

const ParagraphWithAttention = ({attentionType, suffix, icon, ...props}) => {
    const contentWithRemovedSuffix = removeSuffixFromParagraph(props.content, suffix)
    return (
        <div className={`paragraph attention ${attentionType} content-block`}>
            <span className="icon-part">
                <Icon id={icon}/>
                <span className="label-message">{suffix}</span>
            </span>
            <span className="message-part">
                <props.elementsLibrary.DocElement {...props} content={contentWithRemovedSuffix}/>
            </span>
        </div>
    )
}

const NoteParagraph = (props) => <ParagraphWithAttention attentionType="note" suffix={noteSuffix} icon="info" {...props}/>
const WarningParagraph = (props) => <ParagraphWithAttention attentionType="warning" suffix={warningSuffix} icon="alert-triangle" {...props}/>
const QuestionParagraph = (props) => <ParagraphWithAttention attentionType="question" suffix={questionSuffix} icon="help-circle" {...props}/>
const AvoidParagraph = (props) => <ParagraphWithAttention attentionType="avoid" suffix={avoidSuffix} icon="x-octagon" {...props}/>
const DontParagraph = (props) => <ParagraphWithAttention attentionType="avoid" suffix={dontSuffix} icon="x-octagon" {...props}/>
const DoNotParagraph = (props) => <ParagraphWithAttention attentionType="avoid" suffix={doNotSuffix} icon="x-octagon" {...props}/>

const Paragraph = (props) => {
    if (paragraphStartsWith(props.content, noteSuffix)) {
        return <NoteParagraph {...props}/>
    }

    if (paragraphStartsWith(props.content, warningSuffix)) {
        return <WarningParagraph {...props}/>
    }

    if (paragraphStartsWith(props.content, questionSuffix)) {
        return <QuestionParagraph {...props}/>
    }

    if (paragraphStartsWith(props.content, avoidSuffix)) {
        return <AvoidParagraph {...props}/>
    }

    if (paragraphStartsWith(props.content, dontSuffix)) {
        return <DontParagraph {...props}/>
    }

    if (paragraphStartsWith(props.content, doNotSuffix)) {
        return <DoNotParagraph {...props}/>
    }

    return <DefaultParagraph {...props}/>
}

export default Paragraph
