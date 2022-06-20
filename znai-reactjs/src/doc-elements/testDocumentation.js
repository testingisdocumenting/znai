/*
 * Copyright 2020 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export const testDocumentation = {
    "docMeta": {
        "viewOn": {
            "link": "https://github.com/testingisdocumenting/znai/blob/master/znai-docs/znai",
            "title": "View On GitHub"
        },
        "support": {
            "link": "https://github.com/testingisdocumenting/znai/blob/master/znai-docs/znai",
            "title": "Questions/Suggestions"
        },
        "description": "Transforms Markdown files into rich, well-structured documentation served in a modern web front-end. Znai provides many custom add-ons that supercharge basic generated docs with advanced layout features, dynamic references to code and test artifacts, and many visual and functional enhancements.",
        "id": "preview",
        "title": "Znai",
        "type": "User Guide",
        "category": "Documentation",
        "previewEnabled": true
    },
    "page": {
        "type": "Page",
        "content": [{
            "title": "Embedding Content",
            "id": "embedding-content",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "To reduce documentation maintenance burden avoid copy and paste of code snippets.",
                    "type": "SimpleText"
                }, {
                    "type": "SoftLineBreak"
                }, {
                    "text": "Embed content by referencing existing files using the ",
                    "type": "SimpleText"
                }, {
                    "code": ":include-file:",
                    "type": "InlinedCode"
                }, {
                    "text": " plugin instead.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: file-name.js\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "This ",
                    "type": "SimpleText"
                }, {
                    "code": "include-",
                    "type": "InlinedCode"
                }, {
                    "text": " syntax will appear throughout the documentation and represents a family of custom Markdown extensions.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() {\n        usefulAction()\n    }\n}\n\nexport default JsClass",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "The file will be looked up using following rules:",
                    "type": "SimpleText"
                }]
            }, {
                "bulletMarker": "*",
                "tight": true,
                "type": "BulletList",
                "content": [{
                    "type": "ListItem",
                    "content": [{
                        "type": "Paragraph",
                        "content": [{
                            "text": "directory with a markup file",
                            "type": "SimpleText"
                        }]
                    }]
                }, {
                    "type": "ListItem",
                    "content": [{
                        "type": "Paragraph",
                        "content": [{
                            "text": "root directory of a documentation",
                            "type": "SimpleText"
                        }]
                    }]
                }, {
                    "type": "ListItem",
                    "content": [{
                        "type": "Paragraph",
                        "content": [{
                            "text": "all lookup paths listed in a ",
                            "type": "SimpleText"
                        }, {
                            "code": "lookup-paths",
                            "type": "InlinedCode"
                        }, {
                            "text": " file",
                            "type": "SimpleText"
                        }]
                    }]
                }]
            }]
        }, {
            "title": "Syntax highlighting",
            "id": "syntax-highlighting",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "Syntax highlighting is automatically selected based file extension.",
                    "type": "SimpleText"
                }, {
                    "type": "SoftLineBreak"
                }, {
                    "text": "E.g. extensions ",
                    "type": "SimpleText"
                }, {
                    "code": ".c",
                    "type": "InlinedCode"
                }, {
                    "text": ", ",
                    "type": "SimpleText"
                }, {
                    "code": ".h",
                    "type": "InlinedCode"
                }, {
                    "text": ", ",
                    "type": "SimpleText"
                }, {
                    "code": ".cpp",
                    "type": "InlinedCode"
                }, {
                    "text": ", ",
                    "type": "SimpleText"
                }, {
                    "code": ".hpp",
                    "type": "InlinedCode"
                }, {
                    "text": " are treated as C++.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: simple.c\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "c",
                "snippet": "#include <iostream>\n\nusing namespace std;\n\nint main() {\n    cout << \"hello\";\n}",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Use ",
                    "type": "SimpleText"
                }, {
                    "code": "lang",
                    "type": "InlinedCode"
                }, {
                    "text": " to force a different syntax highlighting",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: simple.c {lang: \"java\"}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "java",
                "snippet": "#include <iostream>\n\nusing namespace std;\n\nint main() {\n    cout << \"hello\";\n}",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Note: File extensions and ",
                    "type": "SimpleText"
                }, {
                    "code": "lang",
                    "type": "InlinedCode"
                }, {
                    "text": " are case-insensitive.",
                    "type": "SimpleText"
                }]
            }]
        }, {
            "title": "Title",
            "id": "title",
            "type": "Section",
            "content": [{
                "lang": "",
                "snippet": ":include-file: file-name.js {title: \"ES6 class\"} \n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Use the ",
                    "type": "SimpleText"
                }, {
                    "code": "title",
                    "type": "InlinedCode"
                }, {
                    "text": " property to specify a title.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() {\n        usefulAction()\n    }\n}\n\nexport default JsClass",
                "title": "ES6 class",
                "type": "Snippet"
            }]
        }, {
            "title": "Wide Code",
            "id": "wide-code",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "Use the ",
                    "type": "SimpleText"
                }, {
                    "code": "wide",
                    "type": "InlinedCode"
                }, {
                    "text": " option to stretch wide code to occupy as much horizontal screen real estate as possible.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: WideCode.java {wide: true}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "java",
                "snippet": "class InternationalPriceService implements PriceService {\n    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {\n        ...\n    }\n}",
                "wide": true,
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Without the ",
                    "type": "SimpleText"
                }, {
                    "code": "wide",
                    "type": "InlinedCode"
                }, {
                    "text": " option code will be aligned with the rest of the text and users can use scrollbars.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "java",
                "snippet": "class InternationalPriceService implements PriceService {\n    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {\n        ...\n    }\n}",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Note: Good placement of a ",
                    "type": "SimpleText"
                }, {
                    "type": "Emphasis",
                    "content": [{
                        "text": "Wide Code",
                        "type": "SimpleText"
                    }]
                }, {
                    "text": " element is at the end of a page or a section to show the full version of a code sample.",
                    "type": "SimpleText"
                }]
            }]
        }, {
            "title": "Read More",
            "id": "read-more",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "If you have a file with large code snippet and you want to initially display only a small fraction use ",
                    "type": "SimpleText"
                }, {
                    "code": "readMore",
                    "type": "InlinedCode"
                }, {
                    "text": "",
                    "type": "SimpleText"
                }, {
                    "type": "SoftLineBreak"
                }, {
                    "text": "option with an ",
                    "type": "SimpleText"
                }, {
                    "type": "StrongEmphasis",
                    "content": [{
                        "text": "optional",
                        "type": "SimpleText"
                    }]
                }, {
                    "text": " ",
                    "type": "SimpleText"
                }, {
                    "code": "readMoreVisibleLines",
                    "type": "InlinedCode"
                }, {
                    "text": " option to specify a number of initial lines displayed (default is 8).",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "java",
                "snippet": "public class DocScaffolding {\n    private final Path workingDir;\n    private Map<String, List<String>> fileNameByDirName;\n\n    public DocScaffolding(Path workingDir) {\n        this.workingDir = workingDir;\n        this.fileNameByDirName = new LinkedHashMap<>();\n    }\n\n    public void create() {\n        createPages();\n        createToc();\n        createMeta();\n        createIndex();\n        createLookupPaths();\n    }\n\n    private void createLookupPaths() {\n        createFileFromResource(\"lookup-paths\");\n    }\n\n    private void createMeta() {\n        createFileFromResource(\"meta.json\");\n    }\n}",
                "readMore": true,
                "readMoreVisibleLines": 3,
                "type": "Snippet"
            }]
        }, {
            "title": "Highlights",
            "id": "highlights",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "Use the ",
                    "type": "SimpleText"
                }, {
                    "code": "highlight",
                    "type": "InlinedCode"
                }, {
                    "text": " option to bring readers attention to the important lines.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: file-name.js {highlight: \"export\"}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() {\n        usefulAction()\n    }\n}\n\nexport default JsClass",
                "highlight": [6],
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "It is recommended to pass a substring, but you can pass a line idx (starts from 0).",
                    "type": "SimpleText"
                }, {
                    "type": "SoftLineBreak"
                }, {
                    "text": "Additionally you can combine two approaches and pass a list of things to highlight.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: file-name.js {highlight: [\"export\", 1]}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() {\n        usefulAction()\n    }\n}\n\nexport default JsClass",
                "highlight": [6, 1],
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Note: Order of lines to highlight is reflected during presentation mode",
                    "type": "SimpleText"
                }]
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Use the ",
                    "type": "SimpleText"
                }, {
                    "code": "highlightPath",
                    "type": "InlinedCode"
                }, {
                    "text": " option to highlight lines specified in a separate file.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: file-name.js {highlightPath: \"lines-to-highlight.txt\"}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() {\n        usefulAction()\n    }\n}\n\nexport default JsClass",
                "highlightPath": "lines-to-highlight.txt",
                "highlight": [0, 2],
                "type": "Snippet"
            }, {
                "lang": "txt",
                "snippet": "class\nusefulAction",
                "title": "lines-to-highlight.txt",
                "type": "Snippet"
            }]
        }, {
            "title": "Limit",
            "id": "limit",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "Use ",
                    "type": "SimpleText"
                }, {
                    "code": "startLine",
                    "type": "InlinedCode"
                }, {
                    "text": ", ",
                    "type": "SimpleText"
                }, {
                    "code": "endLine",
                    "type": "InlinedCode"
                }, {
                    "text": " to limit the included content.",
                    "type": "SimpleText"
                }]
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "If you have a file with embedded examples, you can use limit function to extract small samples by using marker lines.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "py",
                "snippet": "import market\n\ndef main():\n    # example: book trade\n    id = market.book_trade('symbol', market.CURRENT_PRICE, 100)\n    # example-end\n\n    # example: cancel trade\n    market.cancel_trade('id')\n    # example-end\n\nif __name__  == \"__main__\":\n    main()",
                "title": "file with examples",
                "type": "Snippet"
            }, {
                "lang": "",
                "snippet": ":include-file: python-examples.py {startLine: \"example: book trade\", endLine: \"example-end\"}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "py",
                "snippet": "# example: book trade\nid = market.book_trade('symbol', market.CURRENT_PRICE, 100)\n# example-end",
                "title": "extracted example",
                "startLine": "example: book trade",
                "endLine": "example-end",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Note: Lines match doesn't have to be exact, ",
                    "type": "SimpleText"
                }, {
                    "code": "contains",
                    "type": "InlinedCode"
                }, {
                    "text": " is used.",
                    "type": "SimpleText"
                }]
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "By default ",
                    "type": "SimpleText"
                }, {
                    "code": "startLine",
                    "type": "InlinedCode"
                }, {
                    "text": " and ",
                    "type": "SimpleText"
                }, {
                    "code": "endLine",
                    "type": "InlinedCode"
                }, {
                    "text": " are included in the rendered result. Use ",
                    "type": "SimpleText"
                }, {
                    "code": "excludeStartEnd: true",
                    "type": "InlinedCode"
                }, {
                    "text": " to remove markers.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: python-examples.py { \n    startLine: \"example: book trade\",\n    endLine: \"example-end\",\n    excludeStartEnd: true }\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "py",
                "snippet": "id = market.book_trade('symbol', market.CURRENT_PRICE, 100)",
                "title": "extracted example",
                "startLine": "example: book trade",
                "endLine": "example-end",
                "excludeStartEnd": true,
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Use ",
                    "type": "SimpleText"
                }, {
                    "code": "includeRegexp",
                    "type": "InlinedCode"
                }, {
                    "text": " to include only lines matching regexp(s).",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: python-examples.py {includeRegexp: \"import\"}\n:include-file: python-examples.py {includeRegexp: [\"import\"]}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "py",
                "snippet": "import market",
                "includeRegexp": ["import"],
                "title": "include by regexp",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Use ",
                    "type": "SimpleText"
                }, {
                    "code": "excludeRegexp",
                    "type": "InlinedCode"
                }, {
                    "text": " to exclude lines matching regexp(s).",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: python-examples.py {excludeRegexp: \"# example\"}\n:include-file: python-examples.py {excludeRegexp: [\"# example\"]}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "lang": "py",
                "snippet": "import market\n\ndef main():\n    id = market.book_trade('symbol', market.CURRENT_PRICE, 100)\n\n    market.cancel_trade('id')\n\nif __name__  == \"__main__\":\n    main()",
                "excludeRegexp": ["# example"],
                "title": "exclude by regexp",
                "type": "Snippet"
            }]
        }, {
            "title": "Callout Comments",
            "id": "callout-comments",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "If you already have comments inside your code it would be non effecient to repeat them inside documentation.",
                    "type": "SimpleText"
                }, {
                    "type": "SoftLineBreak"
                }, {
                    "text": "Instead comments can be automatically extracted and presented as part of the text",
                    "type": "SimpleText"
                }]
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Given file with inlined comments",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() { // new syntax for constructor\n    }\n}\n\nexport default JsClass // new syntax for ES6 modules",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "By specifying ",
                    "type": "SimpleText"
                }, {
                    "code": "commentsType",
                    "type": "InlinedCode"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: file-name-with-comments.js {commentsType: \"inline\"}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "It will be rendered as",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() { // new syntax for constructor\n    }\n}\n\nexport default JsClass // new syntax for ES6 modules",
                "commentsType": "inline",
                "type": "Snippet"
            }]
        }, {
            "title": "Spoilers",
            "id": "spoilers",
            "type": "Section",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "text": "Set the ",
                    "type": "SimpleText"
                }, {
                    "code": "spoiler",
                    "type": "InlinedCode"
                }, {
                    "text": " property to initially hide explanations. It may be useful when teaching.",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "",
                "snippet": ":include-file: file-name-with-comments.js {commentsType: \"inline\", spoiler: true}\n",
                "lineNumber": "",
                "type": "Snippet"
            }, {
                "type": "Paragraph",
                "content": [{
                    "text": "Click on the spoiler to reveal the explanations:",
                    "type": "SimpleText"
                }]
            }, {
                "lang": "javascript",
                "snippet": "class JsClass {\n    constructor() { // new syntax for constructor\n    }\n}\n\nexport default JsClass // new syntax for ES6 modules",
                "commentsType": "inline",
                "spoiler": true,
                "type": "Snippet"
            }]
        }],
        "lastModifiedTime": 1592959343904,
        "tocItem": {
            "sectionTitle": "Snippets",
            "pageTitle": "External Code Snippets",
            "pageMeta": {},
            "dirName": "snippets",
            "fileName": "external-code-snippets",
            "viewOnRelativePath": null,
            "pageSectionIdTitles": [{
                "title": "Embedding Content",
                "id": "embedding-content"
            }, {
                "title": "Syntax highlighting",
                "id": "syntax-highlighting"
            }, {
                "title": "Title",
                "id": "title"
            }, {
                "title": "Wide Code",
                "id": "wide-code"
            }, {
                "title": "Read More",
                "id": "read-more"
            }, {
                "title": "Highlights",
                "id": "highlights"
            }, {
                "title": "Limit",
                "id": "limit"
            }, {
                "title": "Callout Comments",
                "id": "callout-comments"
            }, {
                "title": "Spoilers",
                "id": "spoilers"
            }]
        }
    },
    "footer": {
        "type": "Footer",
        "content": [{
            "type": "Paragraph",
            "content": [{
                "text": "If you have documentation suggestions, features or bugs to report, please create ",
                "type": "SimpleText"
            }, {
                "url": "https://github.com/testingisdocumenting/znai/issues",
                "isFile": false,
                "type": "Link",
                "content": [{
                    "text": "GitHub Issue",
                    "type": "SimpleText"
                }]
            }]
        }, {
            "type": "Paragraph",
            "content": [{
                "text": "Contributions are welcome",
                "type": "SimpleText"
            }]
        }]
    }
};
