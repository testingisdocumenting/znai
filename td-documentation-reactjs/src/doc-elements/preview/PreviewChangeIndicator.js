import {Component} from 'react'

const modifiedClassName = " __recently-modified"

class PreviewChangeIndicator extends Component {
    constructor(props) {
        super(props)
        this.alreadyScrolled = false
    }

    render() {
        this.highlightChanges()
        return null
    }

    componentWillUpdate(nextProps, nextState) {
        if (nextProps.targetDom !== this.props.targetDom) {
            this.alreadyScrolled = false
        }
    }

    highlightChanges() {
        const {targetDom} = this.props
        if (! targetDom) {
            return
        }

        if (this.lastUpdatedDom) {
            this.lastUpdatedDom.className = this.lastUpdatedDom.className.replace(modifiedClassName, "")
        }

        targetDom.className += modifiedClassName

        if (! this.alreadyScrolled && ! elementInViewport(targetDom)) {
            targetDom.scrollIntoView()
            this.alreadyScrolled = true
        }

        this.lastUpdatedDom = targetDom
    }
}

function elementInViewport(el) {
    const rect = el.getBoundingClientRect()
    return (rect.top >= 0 && rect.top <= window.innerHeight)
}

export default PreviewChangeIndicator