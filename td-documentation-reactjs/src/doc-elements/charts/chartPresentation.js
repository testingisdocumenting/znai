import React from 'react'
import {VictoryLabel} from 'victory'
import {isAllAtOnce} from '../meta/meta'

const hiddenStyle = {fill: "rgba(0, 0, 0, 0)"}

const CustomLabel = (props) => {
    return isHidden(props) ?
        <VictoryLabel {...props} style={hiddenStyle}/>:
        <VictoryLabel {...props}/>
}

function isHidden(props) {
    if (isAllAtOnce(props.meta)) {
        return false
    }

    return props.isPresentation && props.index > props.slideIdx
}

export {hiddenStyle, isHidden, CustomLabel}

