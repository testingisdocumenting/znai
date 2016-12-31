const TestData = {
    simplePage: {
        type: "Page",
        docMeta: {
            title: 'Product', type: 'User Guide',
            logo: 'placeholder-logo.png'
        },

        renderContext: {
            nestLevel: 1,
        },

        toc: [
            {
                sectionTitle: 'Introduction', dirName: 'introduction', items: [
                    { title: 'Getting Started', fileName: 'getting-started' },
                    { title: 'First Title', fileName: 'first-title' },
                ]
            },
            {
                sectionTitle: 'Advanced', dirName: 'advanced', items: [
                    { title: 'Super Duper', fileName: 'super-duper' },
                    { title: 'Cook Book', fileName: 'cook-book' },
                ]
            },
        ],

        title: "Sample Page",

        content: [{
            "title": "Section # 1", "type": "Section", "content": [{
                "type": "Paragraph",
                "content": [{
                    "bulletMarker": "*",
                    "tight": true,
                    "type": "BulletList",
                    "content": [
                        {
                            "type": "ListItem",
                            "content": [
                                {
                                    "type": "Paragraph",
                                    "content": [
                                        {
                                            "text": "entry",
                                            "type": "SimpleText",
                                            "content": []
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "ListItem",
                            "content": [
                                {
                                    "type": "Paragraph",
                                    "content": [
                                        {
                                            "text": "another entry",
                                            "type": "SimpleText",
                                            "content": []
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "ListItem",
                            "content": [
                                {
                                    "type": "Paragraph",
                                    "content": [
                                        {
                                            "text": "hello",
                                            "type": "SimpleText",
                                            "content": []
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    "text": "some text goes here", "type": "SimpleText",
                    "content": []
                }]
            }, {
                "lang": "", "lineNumber": "", "snippet":
                "class SampleClass {\n    final int test = 2;\n}\n", "type": "Snippet", "content": []
            }, {
                "componentName": "RestTestOutput", "componentProps": {
                    "data": {
                        "testKey":
                        "testValue"
                    }
                }, "type": "CustomComponent", "content": []
            }, {
                "type":
                "Paragraph", "content": []
            }]
        }, {
            "title": "Section 2", "type": "Section",
            "content": [{
                "type": "Paragraph", "content": [{
                    "text": "another ", "type":
                    "SimpleText", "content": []
                }, {
                    "type": "Emphasis", "content": [{
                        "text": "onestar", "type": "SimpleText", "content": []
                    }]
                }, {
                    "text": " and ", "type": "SimpleText", "content": []
                }, {
                    "type":
                    "StrongEmphasis", "content": [{
                        "text": "twostars", "type":
                        "SimpleText", "content": []
                    }]
                }]
            }]
        }]
    }
};

export default TestData;