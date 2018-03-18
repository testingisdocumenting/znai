/**
 * handles types of objects and build a visual data representation
 * that can be rendered as json tree
 * @param schema schema with additional information like types and items types
 */
export function schemaToJson(schema) {
    return simplifyToJson(schema)
}

function simplifyToJson(schema) {
    if (! schema.hasOwnProperty('type') && schema.hasOwnProperty('properties')) {
        return simplifyObjectToJson(schema)
    }
    
    switch (schema.type) {
        case 'object':
            return simplifyObjectToJson(schema)
        case 'array':
            return simplifyArrayToJson(schema)
        default:
            return schema
    }
}

function simplifyObjectToJson(schema) {
    let result = {}
    Object.keys(schema.properties).forEach(k => {
        result[k] = schemaToJson(schema.properties[k])
    })

    return result
}

function simplifyArrayToJson(schema) {
    return [simplifyToJson(schema.items)]
}
