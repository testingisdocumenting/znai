import React from 'react'

import {CliCommand} from './CliCommand'

export function cliCommandDemo(registry) {
    registry.add('with colons', <CliCommand command="git meta push123 origin HEAD:myfeature/pushrequest some more lines even --more --some-long-options" paramsToHighlight={["push"]}
                                            isPresentation={false}/>)
    registry.add('with brackets', <CliCommand command="git <param1> <param2>" paramsToHighlight={["push"]}
                                              isPresentation={false}/>)
}
