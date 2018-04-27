import React, {Component} from 'react'

import './PreviewChangeIndicator.css'

class PreviewChangeIndicator extends Component {
    state = {}

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps.targetDom !== prevState.targetDom) {
            return {alreadyScrolled: false, targetDom: nextProps.targetDom}
        }

        return null
    }

    componentDidMount() {
        const {targetDom} = this.state

        if (!elementInViewport(targetDom)) {
            targetDom.scrollIntoView()
        }

        this.removeIndicatorTimer = setTimeout(() => {
            this.props.onIndicatorRemove()
        }, 2000)
    }

    componentWillUnmount() {
        clearTimeout(this.removeIndicatorTimer)
    }

    render() {
        const {targetDom} = this.state

        const boundingRect = targetDom.getBoundingClientRect()
        const indicatorStyle = {
            position: 'absolute',
            top: boundingRect.top,
            left: boundingRect.left,
            height: boundingRect.height,
            width: boundingRect.width}

        return (
            <div className="preview-change-indicator" style={indicatorStyle}/>
        )
    }
}

function elementInViewport(el) {
    const rect = el.getBoundingClientRect()
    return (rect.top >= 0 && rect.top <= window.innerHeight)
}

export default PreviewChangeIndicator