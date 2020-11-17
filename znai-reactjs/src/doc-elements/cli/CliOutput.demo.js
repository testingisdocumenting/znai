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

import React from "react";

import {CliOutput} from "./CliOutput";

export function cliOutputDemo(registry) {
    registry
        .add('no highlight', () => (
            <CliOutput lines={generateNonAnsiLines()}/>
        ))
        .add('with empty lines', () => (
            <CliOutput lines={generateNonAnsiWithEmptyLines()}/>
        ))
        .add('with highlight', () => (
            <CliOutput lines={generateNonAnsiLines()} highlight={[1, 'line number 7']}/>
        ))
        .add('with ansi', () => (
            <CliOutput lines={generateAnsiLines()}/>
        ))
        .add('ansi colors', () => (
            <CliOutput lines={generateAllColors()}/>
        ))
        .add('with revealLineStop and highlight outside presentation', () => (
            <CliOutput lines={generateNonAnsiLines()} highlight={[2, 5]} revealLineStop={[0, 2]}/>
        ))
}

function generateNonAnsiLines() {
    return [
        'line number 1',
        'line number 2',
        'line number 3',
        'line number 4',
        'line number 5',
        'line number 6',
        'line number 7',
        'line number 8',
        'line number 9',
        'line number 10',
        'line number 11',
        'line number 12',
    ]
}

function generateNonAnsiWithEmptyLines() {
    return [
        'line number 1',
        'line number 2',
        '',
        'line number 3',
        'line number 4',
        'line number 5',
        '',
        'line number 6',
        'line number 7',
        'line number 8',
        'line number 9',
        'line number 10',
        'line number 11',
        'line number 12',
    ]
}

function generateAnsiLines() {
    return ['\u001B[1mwebtau:\u001B[m000\u001B[1m>\u001B[m http.get("https://jsonplaceholder.typicode.com/todos/1")',
        '\u001B[33m> \u001B[34mexecuting HTTP GET \u001B[35mhttps://jsonplaceholder.typicode.com/todos/1\u001B[0m',
        '  \u001B[32m. \u001B[1mheader.statusCode \u001B[32mequals 200']
}

function generateAllColors() {
    return ['\u001b[30m A \u001b[31m B \u001b[32m C \u001b[33m D \u001b[0m',
        '\u001b[34m E \u001b[35m F \u001b[36m G \u001b[37m H \u001b[0m']
}
