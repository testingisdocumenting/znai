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
                <span className="icon"><Icon id={icon}/></span>
                <span className="label-message">{suffix}</span>
            </span>
            <span className="message-part">
                <props.elementsLibrary.DocElement {...props} content={contentWithRemovedSuffix}/>
            </span>
        </div>
    )
}

const NoteParagraph = (props) => <ParagraphWithAttention attentionType="note" suffix={noteSuffix} icon="info-sign" {...props}/>
const WarningParagraph = (props) => <ParagraphWithAttention attentionType="warning" suffix={warningSuffix} icon="warning-sign" {...props}/>
const QuestionParagraph = (props) => <ParagraphWithAttention attentionType="question" suffix={questionSuffix} icon="question-sign" {...props}/>
const AvoidParagraph = (props) => <ParagraphWithAttention attentionType="avoid" suffix={avoidSuffix} icon="ban-circle" {...props}/>
const DontParagraph = (props) => <ParagraphWithAttention attentionType="avoid" suffix={dontSuffix} icon="ban-circle" {...props}/>
const DoNotParagraph = (props) => <ParagraphWithAttention attentionType="avoid" suffix={doNotSuffix} icon="ban-circle" {...props}/>

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
