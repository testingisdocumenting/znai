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

import { convertAnsiToTokenLines } from './ansiToTokensConverter';

test('convertAnsiToTokenLines', () => {
  const lines = convertAnsiToTokenLines(['\u001B[1mwebtau:\u001B[m000\u001B[1m>\u001B[m http.get("https://jsonplaceholder.typicode.com/todos/1")',
    '\u001B[33m> \u001B[34mexecuting HTTP GET \u001B[35mhttps://jsonplaceholder.typicode.com/todos/1\u001B[0m',
    '  \u001B[32m. \u001B[1mheader.statusCode \u001B[32mequals 200']);

  expect(lines).toEqual([
    [
      { type: 'znai-ansi-bold', content: 'webtau:' },
      { type: 'znai-ansi-regular', content: '000' },
      { type: 'znai-ansi-bold', content: '>' },
      {
        type: 'znai-ansi-regular',
        content: ' http.get("https://jsonplaceholder.typicode.com/todos/1")'
      }
    ],
    [
      { type: 'znai-ansi-yellow-fg', content: '> ' },
      { type: 'znai-ansi-blue-fg', content: 'executing HTTP GET ' },
      {
        type: 'znai-ansi-magenta-fg',
        content: 'https://jsonplaceholder.typicode.com/todos/1'
      },
    ],
    [
      { type: 'znai-ansi-regular', content: '  ' },
      { type: 'znai-ansi-green-fg', content: '. ' },
      { type: 'znai-ansi-bold znai-ansi-green-fg', content: 'header.statusCode ' },
      { type: 'znai-ansi-bold znai-ansi-green-fg', content: 'equals 200' }
    ]
  ]);
})

test('handle empty lines', () => {
  const lines = convertAnsiToTokenLines(['line one', '', 'line two']);
  expect(lines).toEqual([
    [ { type: 'znai-ansi-regular', content: 'line one' } ],
    [],
    [ { type: 'znai-ansi-regular', content: 'line two' } ]
  ])
})