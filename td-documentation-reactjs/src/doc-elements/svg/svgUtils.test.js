import {convertAttrValue} from './svgUtils'

describe("svgUtils", () => {
    it("converts inline style to object", () => {
        const style = convertAttrValue("style", "enable-background:new 0 0 978 978;")
        expect(style).toEqual({enableBackground: "new 0 0 978 978"})
    })
})