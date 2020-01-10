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

import React, {Component} from 'react'

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

        const className = "code-bullets" + (hidden ? " hidden-explanation": "")
        const spoilerMessage = hidden ? (
            <div className="spoiler-message">Press to reveal</div>
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
