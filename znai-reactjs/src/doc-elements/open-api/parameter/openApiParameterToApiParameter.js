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

export function openApiParameterToApiParameter(openApiParameter) {
    const name = openApiParameter.name + (openApiParameter.required ? '*' : '')
    const type = openApiParameter.type
    const description = [...openApiParameter.description,
        ...availableValuesDescription(openApiParameter),
        ...defaultValueDescription(openApiParameter)]

    return {name, type, description}
}


function availableValuesDescription(parameter) {
    if (!parameter.items || !parameter.items.enum) {
        return []
    }

    return [
        {
            type: 'Paragraph', content: [
                {type: 'Emphasis', content: [{type: 'SimpleText', text: 'Available values: '}]},
                {type: 'SimpleText', text: parameter.items.enum.join(', ')}
            ]
        }
    ]
}

function defaultValueDescription(parameter) {
    if (!parameter.items || !parameter.items.default) {
        return []
    }

    return [
        {
            type: 'Paragraph', content: [

                {type: 'Emphasis', content: [{type: 'SimpleText', text: 'Default value: '}]},
                {type: 'SimpleText', text: parameter.items.default}
            ]
        }
    ]
}