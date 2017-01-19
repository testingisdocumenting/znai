import React from 'react'
import {removeCustomProps} from './gvUtils'

const GvPath = ({colors, ...props}) => <path {...removeCustomProps(props)}
                                             stroke={colors.line}/>

export default GvPath