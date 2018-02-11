import {splitFuncNameAndParams} from './langUtils'

describe('langUtils', () => {
    describe('splitFuncNameAndParams', () => {
        it('without params', () => {
            const split = splitFuncNameAndParams('functionName')
            expect(split.funcName).toEqual('functionName')
            expect(split.funcParamsAsStr).toEqual('')
        })

        it('with params', () => {
            const split = splitFuncNameAndParams('functionName(param1, longerParam2) ')
            expect(split.funcName).toEqual('functionName')
            expect(split.funcParamsAsStr).toEqual('param1, longerParam2')
        })
    })
})