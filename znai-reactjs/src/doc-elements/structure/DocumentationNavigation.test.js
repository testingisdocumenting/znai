import {documentationNavigation} from './DocumentationNavigation'
import {setDocMeta} from '../docMeta';

describe("DocumentationNavigation", () => {
    it("extracts page location from url", () => {
        const location = documentationNavigation.extractPageLocation("/something/section-name/page-title#page-section")

        expect(location.dirName).toEqual("section-name")
        expect(location.fileName).toEqual("page-title")
        expect(location.pageSectionId).toEqual("page-section")
    })

    it("extracts page location from url without hash", () => {
        const location = documentationNavigation.extractPageLocation("/something/section-name/page-title")

        expect(location.dirName).toEqual("section-name")
        expect(location.fileName).toEqual("page-title")
        expect(location.pageSectionId).toEqual("")
    })

    it("extracts page location from url where doc id consist of two parts", () => {
        const location = documentationNavigation.extractPageLocation("/doc/id/section-name/page-title#page-section")

        expect(location.dirName).toEqual("section-name")
        expect(location.fileName).toEqual("page-title")
        expect(location.pageSectionId).toEqual("page-section")
    })

    it("extracts page location from url where doc id consist of two parts without hash", () => {
        const location = documentationNavigation.extractPageLocation("/doc/id/section-name/page-title")

        expect(location.dirName).toEqual("section-name")
        expect(location.fileName).toEqual("page-title")
    })

    it("extracts page location for root where doc id consist of two parts", () => {
        setDocMeta({id: 'doc/id'})
        const location = documentationNavigation.extractPageLocation("/doc/id")

        expect(location.dirName).toEqual("")
        expect(location.fileName).toEqual("index")
    })
})