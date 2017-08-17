import React, {Component} from 'react'
import classNames from 'classnames'

import CircleBadge from './CircleBadge'

import {trimComment} from './codeUtils'

const Bullet = ({comment, idx}) => {
    return (
        <div>
            <CircleBadge idx={idx}/>
            <span className="code-bullet-comment">{trimComment(comment)}</span>
        </div>
    )
}

class BulletExplanations extends Component {
    constructor(props) {
        super(props)
        this.state = {hidden: props.spoiler}
    }

    render() {
        const {comments} = this.props
        const {hidden} = this.state

        const className = classNames("code-bullets", {"hidden-explanation": hidden})
        const spoilerMessage = hidden ? (
            <div className="spoiler-message">Click to reveal</div>
        ) : null

        return (
            <div className={className} onClick={this.onSpoilerClick}>
                {spoilerMessage}

                {comments.map((t, idx) => <Bullet key={idx}
                                                  comment={t.content}
                                                  idx={idx + 1}
                                                  hidden={hidden}/>)}
            </div>
        )
    }

    onSpoilerClick = () => {
        this.setState({hidden: false})
    }
}

export default BulletExplanations
