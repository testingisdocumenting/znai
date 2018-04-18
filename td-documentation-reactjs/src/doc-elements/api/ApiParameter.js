import React from 'react'

import ApiParameters from './ApiParameters'

import './ApiParameter.css'

class ApiParameter extends React.Component {
    constructor(props) {
        super(props)

        this.state = { isExpanded: this.props.isExpanded }
    }

    render() {
        const {
            name,
            type,
            children,
            description,
            elementsLibrary
        } = this.props

        const {
            isExpanded
        } = this.state

        const commonClassName = ' api-param-cell' + (isExpanded ? ' expanded' : '')
        const nameTypeClassName = 'api-param-name-type-toggle-cell' + commonClassName
        const descriptionClassName = 'api-param-description-cell' + commonClassName

        const expandToggle = children && (
            <div className="expand-toggle"
                 onClick={this.toggleExpand}>
                {isExpanded ? '-' : '+'}
            </div> )

        const renderedChildren = children && isExpanded && <ApiParameters parameters={children}
                                                                          nested={true}
                                                                          elementsLibrary={elementsLibrary}/>
        return (
            <React.Fragment>
                <div className={nameTypeClassName}>
                    <div className="api-param-name-type-toggle">
                        <div className="name">{name}</div>
                        <div className="type">{type}</div>
                        {expandToggle}
                    </div>
                </div>
                <div className={descriptionClassName}>
                    <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary}/>
                </div>
                {renderedChildren}
            </React.Fragment>
        )
    }

    toggleExpand = () => {
        this.setState(prev => ({isExpanded: !prev.isExpanded}))
    }
}

export default ApiParameter
