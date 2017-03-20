import React from 'react'

const RawHtml = ({html}) => <div dangerouslySetInnerHTML={{ __html: html }}/>

export default RawHtml
