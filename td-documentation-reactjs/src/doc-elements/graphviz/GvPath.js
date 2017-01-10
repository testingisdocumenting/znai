import React from 'react'
import gvUtils from './gvUtils'

const GvPath = ({colors, ...props}) => <path {...gvUtils.removeCustomProps(props)}
                                             stroke={colors.line}/>

export default GvPath