import pageContentProcessor from './pageContentProcessor.js'

describe('page content post processor', () => {
    it('should create an empty section if no section is defined', () => {
        const original = [
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Markdown uses ASCII to represent styles and page structure.",
                        "type": "SimpleText"
                    }
                ]
            }]

        const actual = pageContentProcessor.process(original)

        const expected = [{
            "title": "",
            "id": "",
            "type": "Section",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [
                        {
                            "text": "Markdown uses ASCII to represent styles and page structure.",
                            "type": "SimpleText"
                        }
                    ]
                }]}]

        expect(actual).toEqual(expected)
    })
})