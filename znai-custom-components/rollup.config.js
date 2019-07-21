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

import babel from 'rollup-plugin-babel';
import postcss from 'rollup-plugin-postcss';
import autoprefixer from 'autoprefixer';

export default {
    input: 'src/components/index.js',
    output: {
        file: 'bundle.js',
        format: 'iife',
        name: 'myComponents',
        globals: {'react': 'React'}
    },
    external: [
        'react'
    ],
    plugins: [
        postcss({
            plugins: [autoprefixer()],
            extract: true
        }),
        babel({
            exclude: 'node_modules/**'
        }),
    ]
};