import React from 'react'
import StackTrace from './StackTrace'

const FullStackTrace = ({test}) => <StackTrace message={test.shortStackTrace}/>

export default FullStackTrace