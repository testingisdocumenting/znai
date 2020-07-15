/*
 * Copyright 2020 znai maintainers
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
import { Registry } from 'react-component-viewer';
import { NotAuthorizedScreen } from './NotAuthorizedScreen';

export function notAuthorizedDemo(registry: Registry) {
    registry
      .add('with link and message', () => <NotAuthorizedScreen docId="my-doc"
                                                               allowedGroups={['groupA', 'groupB']}
                                                               authorizationRequestMessage="If you think you should belong to any of the listed groups, consider requesting access to linux group using"
                                                               authorizationRequestLink="https://permission-request-dummy"/>)
      .add('without link', () => <NotAuthorizedScreen docId="my-doc"
                                                      authorizationRequestLink=""
                                                      authorizationRequestMessage=""
                                                      allowedGroups={['groupA', 'groupB']}/>)
}
