/*
 * Copyright 2026 znai maintainers
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

import * as Prism from 'prismjs'

import 'prismjs/components/prism-ocaml'

// prism ocaml grammar leaves module paths like My_module.my_function untokenized.
// add rules to render module names and functions in different colors.
// note: rule names must match tokens.css classes directly since normalizeToken in codeParser drops prism aliases.
// order matters: "function" needs the full "My_module.my_function" text present for its lookbehind,
// so it must run before "class-name" consumes the module part.
// insert before "label" so that in definitions like: let g ~label = ...
// the parameters are still present for the function definition lookahead.
Prism.languages.insertBefore('ocaml', 'label', {
    'function': [
        {
            // lowercase identifier accessed through a module path, e.g. my_function in My_module.my_function
            pattern: /(\b[A-Z][\w']*\s*\.\s*)[a-z_][\w']*/,
            lookbehind: true
        },
        {
            // function definition name followed by parameters, e.g. area in: let area radius = ...
            // value bindings like: let pi = 3.14 are left as is.
            // (?!lazy\b) keeps the keyword in lazy patterns like: let lazy v = ... rendered as keyword
            pattern: /(\b(?:and|let)\s+(?:rec\s+)?)(?!lazy\b)[a-z_][\w']*(?=\s+[a-z_~?(])/,
            lookbehind: true
        }
    ],
    'class-name': [
        {
            // module name in a module path, e.g. My_module in My_module.my_function
            pattern: /\b[A-Z][\w']*(?=\s*\.)/
        },
        {
            // module name after module related keywords, e.g. open My_module
            pattern: /(\b(?:include|module|open)\s+(?:type\s+)?(?:rec\s+)?)[A-Z][\w']*/,
            lookbehind: true
        }
    ]
})
