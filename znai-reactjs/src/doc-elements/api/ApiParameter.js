import React from 'react'

import ApiParameters from './ApiParameters'

import './ApiParameter.css'

class ApiParameter extends React.Component {
    constructor(props) {
        super(props)

        this.state = { isExpanded: this.props.isExpanded, width: 0 }
    }

    render() {
        const {
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

        const commonClassName = ' api-param-cell' +
            (isExpanded ? ' expanded' : '') +
            (children ? ' expandable' : '') +
            (nestedLevel > 0 ? ' nested-' + nestedLevel : '')

        const nameTypeClassName = 'api-param-name-type-toggle-cell' + commonClassName
        const descriptionClassName = 'api-param-description-cell' + commonClassName

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

        return (
            <React.Fragment>
                <div className={nameTypeClassName} onClick={toggleOnClick}>
                    <div className="api-param-name-type-toggle" ref={node => this.nameAndTypeNode = node}>
                        <div className="name">{name}</div>
                        <div className="type-and-toggle">
                            {expandToggle}
                            <div className="type">{type}</div>
                        </div>
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
}

export default ApiParameter
