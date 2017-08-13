import React from 'react'

import {CliCommand} from './CliCommand'

const Demo = () => {
    return <CliCommand command="git meta push origin HEAD:myfeature/pushrequest" paramsToHighlight={["push"]} isPresentation={true}/>
}

export default Demo
