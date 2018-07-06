import React from 'react'

import {printJson} from './jsonPrinter'

import SnippetContainer from '../code-snippets/SnippetContainer'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import './Json.css'

class Json extends React.Component {
    state = {
        previouslyCollapsedPath: []
    }

    render() {
        const {previouslyCollapsedPath} = this.state
        const {data, paths, title, ...props} = this.props

        const lines = printJson({
            rootPath: 'root',
            data,
            paths,
            collapsedPaths: this.collapsedPaths,
            previouslyCollapsedPath: previouslyCollapsedPath,
            onPathUncollapse: this.onPathUncollapse,
            onPathCollapse: this.onPathCollapse })

        return (
            <SnippetContainer linesOfCode={lines}
                              title={title}
                              snippetComponent={SimpleCodeSnippet}
                              {...props}/>
        )
    }

    get collapsedPaths() {
        const {previouslyCollapsedPath} = this.state
        const collapsedPaths = this.props.collapsedPaths
        if (!collapsedPaths) {
            return []
        }

        return collapsedPaths.filter(p => previouslyCollapsedPath.indexOf(p) === -1)
    }

    onPathUncollapse = (path) => {
        this.setState(prev => {
            return {
                previouslyCollapsedPath: [...prev.previouslyCollapsedPath, path]
            }
        })
    }

    onPathCollapse = (path) => {
        this.setState(prev => {
            return {
                previouslyCollapsedPath: prev.previouslyCollapsedPath.filter(p => p !== path),
            }
        })
    }
}

export default Json