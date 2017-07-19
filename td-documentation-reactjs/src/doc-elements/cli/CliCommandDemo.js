import React from 'react'

import {CliCommand} from './CliCommand'

const Demo = () => {
    return <CliCommand command="kubernetes install container --env=prod" paramsToHighlight={["env"]} isPresentation={true}/>
}

export default Demo
