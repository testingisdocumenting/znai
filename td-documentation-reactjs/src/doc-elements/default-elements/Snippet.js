import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import {isInlinedComment} from '../code-snippets/codeUtils'
import {isAllAtOnce} from '../meta/meta'
import {convertToList} from '../propsUtils';

import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const {wide} = this.props

        return wide ? this.renderWideMode() : this.renderNormalMode()
    }

    renderNormalMode() {
        const {title} = this.props

        return (
            <div className="snippet-container content-block">
                <Title title={title}/>

                <div className={this.snippetClassName}>
                    <this.CodeSnippet {...this.props}/>
                </div>
            </div>
        )
    }

    renderWideMode() {
        const {title} = this.props

        const wideModePadding = <div className="padding">&nbsp;</div>

        const className = "snippet-container wide-screen" + (title ? " with-title" : "")
        return (
            <div className={className}>
                {title && <div className="title-layer">
                    {wideModePadding}
                    <Title title={title}/>
                    {wideModePadding}
                </div>}

                <div className={this.snippetClassName}>
                    <this.CodeSnippet {...this.props}/>
                </div>
            </div>
        )
    }

    get CodeSnippet() {
        const {commentsType} = this.props
        return commentsType === 'inline' ? CodeSnippetWithInlineComments : SimpleCodeSnippet
    }

    get snippetClassName() {
        const {title} = this.props
        return "snippet" + (title ? " with-title" : "")
    }
}

function Title({title}) {
    if (!title) {
        return null
    }

    return (
        <div className="title-container content-block">
            <div className="title">{title}</div>
            <div className="large-filling"/>
        </div>
    )
}


const presentationSnippetHandler = {
    component: Snippet,
    numberOfSlides: ({meta, commentsType, tokens, highlight}) => {
        const highlightAsList = convertToList(highlight)

        if (commentsType === 'inline') {
            return inlinedCommentsNumberOfSlides({meta, tokens})
        } else if (highlightAsList.length) {
            return highlightNumberOfSlides({meta, highlightAsList})
        } else {
            return 1
        }
    },

    slideInfoProvider: ({meta, commentsType, tokens, slideIdx}) => {
        if (isAllAtOnce(meta)) {
            return {}
        }

        if (commentsType !== 'inline') {
            return {}
        }

        const comments = tokens.filter(t => isInlinedComment(t))

        return {
            slideVisibleNote: !comments.length ? null :
                slideIdx === 0 ? "" : comments[slideIdx - 1].content
        }
    }
}

function inlinedCommentsNumberOfSlides({meta, tokens}) {
    const comments = tokens.filter(t => isInlinedComment(t))

    if (isAllAtOnce(meta) && comments.length > 0) {
        return 2 // two slides: 1st - no highlights; 2nd - all highlighted at once
    }

    return comments.length + 1
}

function highlightNumberOfSlides({meta, highlightAsList}) {
    if (isAllAtOnce(meta) && highlightAsList.length > 0) {
        return 2 // two slides: 1st - no highlights; 2nd - all highlighted at once
    }

    return highlightAsList.length + 1
}

export {Snippet, presentationSnippetHandler}
