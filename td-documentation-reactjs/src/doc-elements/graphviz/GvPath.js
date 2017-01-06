import React from 'react'

const GvPath = ({colors, ...props}) => <path {...props} stroke={colors.line}/>

export default GvPath