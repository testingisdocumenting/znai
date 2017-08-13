import React from 'react'

import {CliCommand} from './CliCommand'

const Demo = () => {
    return <CliCommand command="git meta push origin HEAD:myfeature/pushrequest some more lines even --more --some-long-options" paramsToHighlight={["push"]}
                       isPresentation={true}/>
}

export default Demo
