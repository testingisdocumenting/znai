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
import {textSelection} from '../selected-text-extensions/TextSelection'

import './Section.css'

class Section extends Component {
    render() {
        const {id, title, ...props} = this.props

        const sectionTitle = (
            <div className="content-block">
                <props.elementsLibrary.SectionTitle id={id} title={title || ''}/>
            </div>
        )

        return (
            <div className="section" key={title} ref={n => this.node = n}>
                {sectionTitle}
                <props.elementsLibrary.DocElement {...props}/>
            </div>
        )
    }

    componentDidMount() {
        this.node.addEventListener('mousedown', this.mouseDownHandler)
    }

    componentWillUnmount() {
        this.node.removeEventListener('mousedown', this.mouseDownHandler)
    }

    mouseDownHandler = () => {
        const {id, title} = this.props

        textSelection.startSelection({pageSectionId: id, pageSectionTitle: title})
    }
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const presentationSectionHandler = {
    component: PresentationTitle,
    numberOfSlides: () => 1,
    slideInfoProvider: ({title}) => {
        return {sectionTitle: title}
    }
}

export {Section, presentationSectionHandler}
