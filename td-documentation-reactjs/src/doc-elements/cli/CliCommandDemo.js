import React from 'react'

import {CliCommand} from './CliCommand'

const Demo = () => {
    return <CliCommand command="git meta push123 origin HEAD:myfeature/pushrequest some more lines even --more --some-long-options" paramsToHighlight={["push"]}
                       isPresentation={false}/>
}

export default Demo
