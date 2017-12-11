import {convertToList} from './propsUtils';

describe('propsUtils', () => {
    it('convertToList', () => {
        expect(convertToList(null)).toEqual([])
        expect(convertToList(undefined)).toEqual([])
        expect(convertToList('v')).toEqual(['v'])
        expect(convertToList(['v'])).toEqual(['v'])
    })
});
