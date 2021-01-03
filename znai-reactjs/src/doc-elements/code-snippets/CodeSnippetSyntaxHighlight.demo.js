/*
 * Copyright 2020 znai maintainers
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

import {Snippet} from './Snippet'

export function syntaxHighlightSnippetDemo(registry) {
    registry
        .add('yaml', () => <Snippet snippet={yamlCode()} lang="yaml" highlight={[3, 5]}/>)
        .add('protobuf', () => <Snippet snippet={protoCode()} lang="proto" highlight={[5]}/>)
        .add('graphql', () => <Snippet snippet={graphqlCode()} lang="graphql" highlight={[5]}/>)
        .add('diff', () => <Snippet snippet={diffCode()} lang="diff"/>)
        .add('diff-javascript', () => <Snippet snippet={diffJavaScriptCode()} lang="diff-javascript"/>)
}

function yamlCode() {
    return 'invoice: 34843\n' +
        'date   : 2001-01-23\n' +
        'bill-to: &id001\n' +
        '    given  : Chris\n' +
        '    family : Dumars\n' +
        '    address:\n' +
        '        lines: |\n' +
        '            458 Walkman Dr.\n' +
        '            Suite #292\n' +
        '        city    : Royal Oak\n' +
        '        state   : MI\n' +
        '        postal  : 48046\n' +
        'comments: >\n' +
        '    Late afternoon is best.\n' +
        '    Backup contact is Nancy\n' +
        '    Billsmer @ 338-4338.'
}

function protoCode() {
    return 'syntax = "proto2";\n' +
        '\n' +
        'package tutorial;\n' +
        '\n' +
        'message Person {\n' +
        '  required string name = 1;\n' +
        '  required int32 id = 2;\n' +
        '  optional string email = 3;\n' +
        '\n' +
        '  enum PhoneType {\n' +
        '    MOBILE = 0;\n' +
        '    HOME = 1;\n' +
        '    WORK = 2;\n' +
        '  }\n' +
        '\n' +
        '  message PhoneNumber {\n' +
        '    required string number = 1;\n' +
        '    optional PhoneType type = 2 [default = HOME];\n' +
        '  }\n' +
        '\n' +
        '  repeated PhoneNumber phones = 4;\n' +
        '}\n' +
        '\n' +
        'message AddressBook {\n' +
        '  repeated Person people = 1;\n' +
        '}'
}

function graphqlCode() {
    return 'type Query {\n' +
        '  me: User\n' +
        '}\n' +
        ' \n' +
        'type User {\n' +
        '  id: ID\n' +
        '  name: String\n' +
        '}'
}

function diffCode() {
    return '*** /path/to/original\ttimestamp\n' +
        '--- /path/to/new\ttimestamp\n' +
        '***************\n' +
        '*** 1,3 ****\n' +
        '--- 1,9 ----\n' +
        '+ This is an important\n' +
        '+ notice! It should\n' +
        '+ therefore be located at\n' +
        '+ the beginning of this\n' +
        '+ document!\n' +
        '+\n' +
        '  This part of the\n' +
        '  document has stayed the\n' +
        '  same from version to\n' +
        '***************\n' +
        '*** 8,20 ****\n' +
        '  compress the size of the\n' +
        '  changes.\n' +
        '\n' +
        '- This paragraph contains\n' +
        '- text that is outdated.\n' +
        '- It will be deleted in the\n' +
        '- near future.\n' +
        '\n' +
        '  It is important to spell\n' +
        '! check this dokument. On\n' +
        '  the other hand, a\n' +
        '  misspelled word isn\'t\n' +
        '  the end of the world.\n' +
        '--- 14,21 ----\n' +
        '  compress the size of the\n' +
        '  changes.\n' +
        '\n' +
        '  It is important to spell\n' +
        '! check this document. On\n' +
        '  the other hand, a\n' +
        '  misspelled word isn\'t\n' +
        '  the end of the world.\n' +
        '***************\n' +
        '*** 22,24 ****\n' +
        '--- 23,29 ----\n' +
        '  this paragraph needs to\n' +
        '  be changed. Things can\n' +
        '  be added after it.\n' +
        '+\n' +
        '+ This paragraph contains\n' +
        '+ important new additions\n' +
        '+ to this document.'
}

function diffJavaScriptCode() {
    return '@@ -4,6 +4,5 @@\n' +
        '-    let foo = bar.baz([1, 2, 3]);\n' +
        '-    foo = foo + 1;\n' +
        '+    const foo = bar.baz([1, 2, 3]) + 1;\n'
}