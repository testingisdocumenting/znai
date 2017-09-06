import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import {isInlinedComment} from '../code-snippets/codeUtils'
import {isAllAtOnce} from '../meta/meta'

import './Snippet.css'

const Title = ({title}) => {
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

class Snippet extends React.Component {
    render() {
        const {maxLineLength} = this.props
        const isWide = maxLineLength && maxLineLength >= 115;

        return isWide ? this.renderWideMode() : this.renderNormalMode()
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

        return (
            <div className="snippet-container wide-screen">
                <div className="title-layer">
                    {wideModePadding}
                    <Title title={title}/>
                    {wideModePadding}
                </div>

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

const presentationSnippetHandler = {
    component: Snippet,
    numberOfSlides: ({meta, commentsType, tokens}) => {
        if (commentsType !== 'inline') {
            return 1
        }

        const comments = tokens.filter(t => isInlinedComment(t))

        if (isAllAtOnce(meta) && comments.length > 0) {
            return 2 // no highlights and all highlighted at once
        }

        return comments.length + 1
    },

    slideInfoProvider: ({meta, tokens, slideIdx}) => {
        const comments = tokens.filter(t => isInlinedComment(t))

        if (isAllAtOnce(meta)) {
            return {}
        }

        return {
            slideVisibleNote: !comments.length ? null :
                slideIdx === 0 ? "" : comments[slideIdx - 1].content
        }
    }
}

export {Snippet, presentationSnippetHandler}
