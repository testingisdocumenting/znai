import {splitContentInTwoPartsSubsections} from './twoSidesContentSplitter'

describe('splitContentInTwoPartsSubsections', () => {
    it('splits by first entry of second-side meta', () => {
        const subSections = splitContentInTwoPartsSubsections(oneSubSectionContent())

        expect(subSections.length).toEqual(1)

        const firstSubSection = subSections[0]
        expect(firstSubSection.left).toEqual([{
            "type": "Paragraph",
            "content": [
                {
                    "text": "Markdown uses ASCII to represent styles and page structure.",
                    "type": "SimpleText",
                }
            ],
        }])
        expect(firstSubSection.right).toEqual([{
            "type": "Paragraph",
            "meta": {
                "rightSide": true,
            },
            "content": [
                {
                    "text": "Another paragraph",
                    "type": "SimpleText",
                    "meta": {
                        "rightSide": true,
                    },
                }
            ],
        }])
    })
})

function oneSubSectionContent() {
    return [
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": "Markdown uses ASCII to represent styles and page structure.",
                    "type": "SimpleText",
                }
            ],
        },
        {
            "type": "Paragraph",
            "meta": {
                "rightSide": true,
            },
            "content": [
                {
                    "text": "Another paragraph",
                    "type": "SimpleText",
                    "meta": {
                        "rightSide": true,
                    },
                }
            ],
        }]

}
