import React, {Component} from 'react'

import CircleBadge from './CircleBadge'

import {trimComment} from './codeUtils'

const Bullet = ({comment, idx}) => {
    return <div><CircleBadge idx={idx}/> <span className="code-bullet-comment">{trimComment(comment)}</span></div>
}

class BulletExplanations extends Component {
    constructor(props) {
        super(props)
        this.state = {revealed: !props.spoiler}
    }

    render() {
        const {comments} = this.props
        const {revealed} = this.state

        return revealed ? (
            <div className="code-bullets">
                {comments.map((t, idx) => <Bullet key={idx} comment={t.content} idx={idx + 1}/>)}
            </div>
        ) : (
            <div className="spoiler" onClick={this.onSpoilerClick}>Click to reveal</div>
        )
    }

    onSpoilerClick = () => {
        this.setState({revealed: true})
    }
}

export default BulletExplanations
