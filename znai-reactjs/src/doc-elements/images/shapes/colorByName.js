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

const colors = {
    white: {
        stroke: "#ddd",
        fill: "#e6e6e6",
        text: "#555"
    },
    red: {
        stroke: "#9e1e15",
        fill: "#d65100",
        text: "#fc957a"
    },
    green: {
        stroke: "#35524A",
        fill: "#8DAB7F",
        text: "#eee"
    },
    blue: {
        stroke: "#273043",
        fill: "#5D707F",
        text: "#eee"
    },
    yellow: {
        fill: "#FFE400",
        stroke: "#4A493E",
        text: "#eee"
    }
}

export default (name) => {
    const color = colors[name]
    return color ? color : colors.red
}