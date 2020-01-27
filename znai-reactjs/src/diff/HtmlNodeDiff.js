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

import * as Diff from 'diff'

import {HtmlNodeFlattenedList} from './HtmlNodeFlattenedList'

export class HtmlNodeDiff {
    constructor(beforeNodeRoot, afterNodeRoot) {
        this.beforeNodeRoot = beforeNodeRoot
        this.afterNodeRoot = afterNodeRoot

        this.beforeNodeList = new HtmlNodeFlattenedList(this.beforeNodeRoot)
        this.afterNodeList = new HtmlNodeFlattenedList(this.afterNodeRoot)

        this.diffResultRaw = Diff.diffArrays(
            this.beforeNodeList.list,
            this.afterNodeList.list, {comparator: compareListEntry})

        this.addedNodes = this._extractAddedNodes()
        this.addedNodesContainers = this._extractAddedNodesContainers()

        console.log(this.diffResultRaw)
    }

    animateAdded() {
        const nodesToHighlight = [...this.addedNodesContainers, ...this.addedNodes]
        nodesToHighlight.forEach(node => {
            addAnimationClass(node)
            node.addEventListener('animationend', removeAnimationClass)
        })
    }

    scrollAddedIntoView(nodeWithScroll) {
        if (this.addedNodes.length === 0) {
            return
        }

        const isAnyChangeVisible = this.addedNodes.some(node => isNodeInViewport(nodeWithScroll, node))
        if (isAnyChangeVisible) {
            return
        }

        scrollIntoView(nodeWithScroll, this.addedNodes[0])
    }

    _extractAddedNodes() {
        return this.diffResultRaw
            .filter(diffEntry => diffEntry.added)
            .flatMap(diffEntry => diffEntry.value)
            .map(addEntryValue => addEntryValue.node)
    }

    _extractAddedNodesContainers() {
        const containersSet = this.diffResultRaw
            .filter(diffEntry => diffEntry.added)
            .flatMap(diffEntry => diffEntry.value)
            .filter(diffEntry => diffEntry.container !== null)
            .reduce((set, addEntryValue) => {
                set.add(addEntryValue.container)
                return set
            }, new Set())

        return [...containersSet]
    }
}

function isNodeInViewport(containerNode, node) {
    const containerRect = containerNode.getBoundingClientRect()
    const rect = node.getBoundingClientRect()

    return rect.top > containerRect.top && rect.top < (containerRect.bottom - 10)
}

function scrollIntoView(nodeWithScroll, node) {
    const containerRect = nodeWithScroll.getBoundingClientRect()
    const rect = node.getBoundingClientRect()

    const scrollDelta = containerRect.top - rect.top + (0.33 * containerRect.height)
    nodeWithScroll.scrollTop -= scrollDelta
}

function compareListEntry(a, b) {
    return a.value === b.value
}

const animationClassName = 'znai-preview-change-tracker-indicator'

function removeAnimationClass(animation) {
    removeClass(animation.target, animationClassName)
}

function addAnimationClass(node) {
    if (node.className.indexOf(animationClassName) !== -1) {
        return
    }

    const optionalSpace = node.className.length === 0 ? '' : ' '
    node.className += optionalSpace + animationClassName
}

function removeClass(node, className) {
    const parts = node.className.split(/\s+/)
    node.className = parts.filter(p => p !== className).join(' ')
}
