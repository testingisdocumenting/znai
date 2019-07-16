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

        const actual = pageContentProcessor.process(original)
        expect(actual).toEqual(expected)
    })

    it('should merge meta props into a next doc element and all its children', () => {
        const original = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Meta",
                        "extraPropA": "value1",
                        "extraPropB": "value2"
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Markdown uses ASCII to represent styles and page structure.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Another paragraph",
                                "type": "SimpleText"
                            }
                        ]
                    }]
            }]

        const expected  = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "meta": {
                            "extraPropA": "value1",
                            "extraPropB": "value2"
                        },
                        "content": [
                            {
                                "text": "Markdown uses ASCII to represent styles and page structure.",
                                "type": "SimpleText",
                                "meta": {
                                    "extraPropA": "value1",
                                    "extraPropB": "value2"
                                }
                            }
                        ],
                    },
                    {
                        "type": "Paragraph",
                        "meta": {
                            "extraPropA": "value1",
                            "extraPropB": "value2"
                        },
                        "content": [
                            {
                                "text": "Another paragraph",
                                "type": "SimpleText",
                                "meta": {
                                    "extraPropA": "value1",
                                    "extraPropB": "value2"
                                }
                            }
                        ],
                    }]
            }]

        const actual = pageContentProcessor.process(original)
        expect(actual).toEqual(expected)
    })
})