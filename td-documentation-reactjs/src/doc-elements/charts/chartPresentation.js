import React from 'react'
import {VictoryLabel} from 'victory'

const hiddenStyle = {fill: "rgba(0, 0, 0, 0)"}

const CustomLabel = (props) => {
    return isHidden(props) ?
        <VictoryLabel {...props} style={hiddenStyle}/>:
        <VictoryLabel {...props}/>
}

function isHidden(props) {
    return props.isPresentation && props.index > props.slideIdx
}

export {hiddenStyle, isHidden, CustomLabel}

