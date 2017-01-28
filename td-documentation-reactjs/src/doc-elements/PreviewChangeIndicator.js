import {Component} from 'react'

class PreviewChangeIndicator extends Component {
    render() {
        this.highlightChanges()
        return null
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

        if (! elementInViewport(targetDom)) {
            targetDom.scrollIntoView()
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