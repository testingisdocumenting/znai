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

/**
 * global references are defined on the documentation level and is applicable to every code snippet.
 * references is loaded as a javascript file with a globally defined variable (similar to how Table Of Contents is loaded)
 *
 * @param localReferences local references, can be null
 * @returns {Object} global references merged with local, null if neither is defined
 */
export function mergeWithGlobalDocReferences(localReferences) {
    if (window.docReferences !== null) {
        return {...window.docReferences, ...localReferences}
    }

    return localReferences
}

export function updateGlobalDocReferences(docReferences) {
    window.docReferences = docReferences
}