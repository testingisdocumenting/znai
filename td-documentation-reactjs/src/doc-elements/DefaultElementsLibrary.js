import React from 'react'
import DocElement from './DocElement'
import Page from './Page'

const library = {}

const BoundDocElement = ({content}) => <DocElement content={content} elementsLibrary={library} />

library.Emphasis = ({content}) => (<span className="emphasis"><BoundDocElement content={content} /></span>)
library.StrongEmphasis = ({content}) => (<span className="strong-emphasis"><BoundDocElement content={content} /></span>)
library.Link = ({anchor, label}) => (<a href={this.props.anchor}>{this.props.label}</a>)
library.Paragraph = ({content}) => <div className="paragraph"><BoundDocElement content={content} /></div>
library.SimpleText = ({text}) => <span className="simple-text">{text}</span>

library.Section = ({title, content}) => (
    <div className="section">
        <div className="section-title">{title}</div>
        <BoundDocElement content={content} />
    </div>)

library.CustomComponent = ({componentName, componentProps}) => {
    const Component = library[componentName] // TODO additional lib for provided custom components
    if (! Component) {
        console.warn("can't find a custom component: ", componentName)
        return null
    } else {
        return <Component {...componentProps} />;
    }
}

library.Page = Page;

export default library
