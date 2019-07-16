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