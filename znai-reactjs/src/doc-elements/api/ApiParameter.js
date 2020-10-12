/*
 * Copyright 2020 znai maintainers
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

import ApiParameters from './ApiParameters'
import {ReferenceLinkWrapper} from '../references/ReferenceLinkWrapper'

import {Icon} from '../icons/Icon';
import './ApiParameter.css'

class ApiParameter extends React.Component {
    constructor(props) {
        super(props)

        this.state = { isExpanded: this.props.isExpanded, width: 0 }
    }

    render() {
        const {
            anchorId,
            name,
            type,
            children,
            description,
            nestedLevel = 0,
            elementsLibrary
        } = this.props

        const {
            isExpanded,
            width
        } = this.state

        const reference = this.findReference()

        const commonClassName = ' znai-api-param-cell' +
            (isExpanded ? ' expanded' : '') +
            (children ? ' expandable' : '') +
            (reference ? ' with-reference' : '') +
            (nestedLevel > 0 ? ' nested-' + nestedLevel : '')

        const nameTypeClassName = 'znai-api-param-name-type-toggle-cell' + commonClassName
        const descriptionClassName = 'znai-api-param-description-cell' + commonClassName

        const toggleOnClick = children ? this.toggleExpand : null

        const expandToggle = children && (
            <div className="expand-toggle">
                {isExpanded ? '-' : '+'}
            </div>)

        const renderedChildren = (children && isExpanded) ? (
            <React.Fragment>
                <div/>
                <ApiParameters parameters={children}
                               nestedLevel={nestedLevel + 1}
                               parentWidth={width}
                               elementsLibrary={elementsLibrary}/>
            </React.Fragment>
        ): null

        const referenceUrl = reference ? reference.pageUrl : null

        return (
            <React.Fragment>
                <div className={nameTypeClassName}>
                    <div className="znai-api-param-name-type-toggle" ref={node => this.nameAndTypeNode = node}>
                        <div className="znai-api-param-name">
                            <ReferenceLinkWrapper referenceUrl={referenceUrl}>
                                {name}
                            </ReferenceLinkWrapper>
                        </div>
                        <div className="znai-api-param-type-and-toggle" onClick={toggleOnClick}>
                            {expandToggle}
                            <div className="znai-api-param-type">
                                {type}
                            </div>
                        </div>
                    </div>
                    <div className="znai-api-param-anchor" id={anchorId}>
                        {anchorId && <a href={"#" + anchorId}><Icon id="link"/></a>}
                    </div>
                </div>
                <div className={descriptionClassName}>
                    <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary}/>
                </div>
                {renderedChildren}
            </React.Fragment>
        )
    }

    componentDidMount() {
        const width = this.nameAndTypeNode.getBoundingClientRect().width
        this.setState({width})
    }

    toggleExpand = () => {
        this.setState(prev => ({isExpanded: !prev.isExpanded}))
    }

    findReference() {
        const {references, type, name} = this.props
        if (!references) {
            return null
        }

        const byType = references[type]
        if (byType) {
            return byType
        }

        const byName = references[name]
        if (byName) {
            return byName
        }
    }
}

export default ApiParameter
