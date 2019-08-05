/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'

import {CliCommand} from './CliCommand'

export function cliCommandDemo(registry) {
    registry.add('with colons', () => <CliCommand
        command="git meta push123 origin HEAD:myfeature/pushrequest some more lines even --more --some-long-options"
        paramsToHighlight={["push"]}
        isPresentation={false}/>)
    registry.add('with brackets', () => <CliCommand command="git <param1> <param2>" paramsToHighlight={["push"]}
                                                    isPresentation={false}/>)
}