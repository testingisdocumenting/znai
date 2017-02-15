import {Component} from 'react'

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
            this.lastUpdatedDom.className = this.lastUpdatedDom.className.replace(" __recently-modified", "")
        }

        targetDom.className += " __recently-modified"

        if (! this.alreadyScrolled && ! elementInViewport(targetDom)) {
            targetDom.scrollIntoView()
            this.alreadyScrolled = true
        }

        this.lastUpdatedDom = targetDom
    }
}

function elementInViewport(el) {
    const rect = el.getBoundingClientRect()

    return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.right <= (window.innerWidth || document.documentElement.clientWidth))
}

export default PreviewChangeIndicator