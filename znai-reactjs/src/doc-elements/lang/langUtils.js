export function splitFuncNameAndParams(funcWithParams) {
    const startIdx = funcWithParams.indexOf('(')
    const endIdx = funcWithParams.lastIndexOf(')')

    if (startIdx === -1 || endIdx === -1) {
        return {funcName: funcWithParams, funcParamsAsStr: ''}
    }

    return {
        funcName: funcWithParams.substr(0, startIdx),
        funcParamsAsStr: funcWithParams.substr(startIdx + 1, (endIdx - startIdx - 1))
    }
}