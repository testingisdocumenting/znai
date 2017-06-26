import {documentationNavigation} from './DocumentationNavigation'

describe("DocumentationNavigation", () => {
    it("extracts page location from url", () => {
        const location = documentationNavigation.extractPageLocation("http://localhost/something/section-name/page-title#page-section")

        expect(location.dirName).toEqual("section-name")
        expect(location.fileName).toEqual("page-title")
        expect(location.pageSectionId).toEqual("page-section")
    })

    it("extracts page location from url without hash", () => {
        const location = documentationNavigation.extractPageLocation("http://localhost/something/section-name/page-title")

        expect(location.dirName).toEqual("section-name")
        expect(location.fileName).toEqual("page-title")
        expect(location.pageSectionId).toEqual("")
    })
})