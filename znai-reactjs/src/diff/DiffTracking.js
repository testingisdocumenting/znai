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

import * as React from 'react'
import {HtmlNodeDiff} from './HtmlNodeDiff'

import './DiffTracking.css'
import {mainPanelClassName} from '../layout/classNames';

let enabled = false
let autoDisable = false

export class DiffTracking extends React.Component {
    render() {
        const {children} = this.props

        return (
            <div className="znai-diff-highlight" ref={this.saveRootNode}>
                {children}
            </div>
        )
    }

    saveRootNode = (node) => {
        if (!node) { // TODO investigate why
            return
        }

        this.rootNode = node.querySelector('.page-content')
        this.scrollNode = node.querySelector('.' + mainPanelClassName)
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        if (!enabled) {
            return null
        }

        return {
            beforeNode: this.rootNode.cloneNode(true),
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (!snapshot) {
            return
        }

        setTimeout(() => {
            try {
                const diffNode = new HtmlNodeDiff(snapshot.beforeNode, this.rootNode)
                diffNode.scrollAddedIntoView(this.scrollNode)
                diffNode.animateAdded()
            } finally {
                if (autoDisable) {
                    enabled = false
                    autoDisable = false
                }
            }
        }, 50)
    }
}

export function enableDiffTracking() {
    enabled = true
}

export function disableDiffTracking() {
    enabled = false
}

export function enableDiffTrackingForOneDomChangeTransaction() {
    enabled = true
    autoDisable = true
}
