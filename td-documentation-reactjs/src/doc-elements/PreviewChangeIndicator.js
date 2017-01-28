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
            this.lastUpdatedDom.className = targetDom.className.replace(" __recently-modified", "")
        }

        targetDom.className += " __recently-modified"
        targetDom.scrollIntoView()
        this.lastUpdatedDom = targetDom
    }
}

export default PreviewChangeIndicator