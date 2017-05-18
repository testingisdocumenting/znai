export default {allPages: [{
    "type": "Page",
    "content": [{
        "title": "DDJT",
        "id": "ddjt",
        "type": "Section",
        "content": [{
            "type": "Paragraph",
            "content": [{
                "text": "Data Driven Java Testing: Java and Groovy API to write data driven Unit/Component/Integration tests test your JVM based Applications.",
                "type": "SimpleText"
            }]
        }]
    }],
    "tocItem": {
        "sectionTitle": "",
        "pageTitle": "Index",
        "fileName": "index",
        "dirName": "",
        "pageSectionIdTitles": [{"title": "DDJT", "id": "ddjt"}]
    }
}, {
    "type": "Page",
    "content": [{
        "title": "Data Injection",
        "id": "data-injection",
        "type": "Section",
        "content": [{
            "type": "Paragraph",
            "content": [{
                "text": "Let\u0027s consider an example.",
                "type": "SimpleText"
            }, {"type": "SoftLineBreak"}, {
                "code": "MarginCalculator",
                "type": "InlinedCode"
            }, {"text": " class with ", "type": "SimpleText"}, {
                "code": "calculateMargin",
                "type": "InlinedCode"
            }, {"text": " method that requires a list of ", "type": "SimpleText"}, {
                "code": "Transaction",
                "type": "InlinedCode"
            }, {"text": " objects.", "type": "SimpleText"}]
        }, {
            "type": "Paragraph",
            "content": [{"text": "One possible design of ", "type": "SimpleText"}, {
                "code": "MarginCalculator",
                "type": "InlinedCode"
            }, {"text": ":", "type": "SimpleText"}]
        }, {
            "bulletMarker": "*",
            "tight": true,
            "type": "BulletList",
            "content": [{
                "type": "ListItem",
                "content": [{
                    "type": "Paragraph",
                    "content": [{"text": "fetch required data by itself", "type": "SimpleText"}]
                }]
            }, {
                "type": "ListItem",
                "content": [{
                    "type": "Paragraph",
                    "content": [{
                        "type": "Emphasis",
                        "content": [{"text": "Inject", "type": "SimpleText"}]
                    }, {"text": " ", "type": "SimpleText"}, {
                        "code": "DAO",
                        "type": "InlinedCode"
                    }, {"text": " to decouple", "type": "SimpleText"}]
                }]
            }]
        }, {
            "lang": "java",
            "maxLineLength": 62,
            "tokens": [{"type": "keyword", "content": "public"}, " ", {
                "type": "keyword",
                "content": "class"
            }, " ", {"type": "class-name", "content": ["MarginCalculator"]}, " ", {
                "type": "punctuation",
                "content": "{"
            }, "\n    ", {"type": "keyword", "content": "public"}, " ", {
                "type": "function",
                "content": "MarginCalculator"
            }, {
                "type": "punctuation",
                "content": "("
            }, "TransactionsDao transactionsDao", {
                "type": "punctuation",
                "content": ")"
            }, " ", {"type": "punctuation", "content": "{"}, "\n    ", {
                "type": "punctuation",
                "content": "}"
            }, "\n    \n    ", {"type": "keyword", "content": "public"}, " ", {
                "type": "keyword",
                "content": "double"
            }, " ", {"type": "function", "content": "calculate"}, {
                "type": "punctuation",
                "content": "("
            }, "List", {"type": "operator", "content": "\u003c"}, "String", {
                "type": "operator",
                "content": "\u003e"
            }, " ids", {"type": "punctuation", "content": ")"}, " ", {
                "type": "punctuation",
                "content": "{"
            }, "\n    ", {"type": "punctuation", "content": "}"}, "\n", {
                "type": "punctuation",
                "content": "}"
            }, "\n"],
            "lineNumber": "",
            "type": "Snippet"
        }, {
            "type": "Paragraph",
            "content": [{"text": "Lets make design even simpler for testing:", "type": "SimpleText"}]
        }, {
            "bulletMarker": "*",
            "tight": true,
            "type": "BulletList",
            "content": [{
                "type": "ListItem",
                "content": [{
                    "type": "Paragraph",
                    "content": [{
                        "type": "Emphasis",
                        "content": [{"text": "Inject", "type": "SimpleText"}]
                    }, {"text": " required data", "type": "SimpleText"}]
                }]
            }]
        }, {
            "lang": "java",
            "maxLineLength": 62,
            "tokens": [{"type": "keyword", "content": "public"}, " ", {
                "type": "keyword",
                "content": "class"
            }, " ", {"type": "class-name", "content": ["MarginCalculator"]}, " ", {
                "type": "punctuation",
                "content": "{"
            }, "\n    ", {"type": "keyword", "content": "public"}, " ", {
                "type": "keyword",
                "content": "double"
            }, " ", {"type": "function", "content": "calculate"}, {
                "type": "punctuation",
                "content": "("
            }, "List", {"type": "operator", "content": "\u003c"}, "Transactions", {
                "type": "operator",
                "content": "\u003e"
            }, " transactions", {"type": "punctuation", "content": ")"}, " ", {
                "type": "punctuation",
                "content": "{"
            }, "\n    ", {"type": "punctuation", "content": "}"}, "\n", {
                "type": "punctuation",
                "content": "}"
            }, "\n"],
            "lineNumber": "",
            "type": "Snippet"
        }, {
            "type": "Paragraph",
            "content": [{"text": "This version is much easier to test:", "type": "SimpleText"}]
        }, {
            "bulletMarker": "*",
            "tight": true,
            "type": "BulletList",
            "content": [{
                "type": "ListItem",
                "content": [{
                    "type": "Paragraph",
                    "content": [{"text": "Less mocks/stubs", "type": "SimpleText"}]
                }]
            }, {
                "type": "ListItem",
                "content": [{"type": "Paragraph", "content": [{"text": "Less wiring", "type": "SimpleText"}]}]
            }]
        }, {
            "type": "Paragraph",
            "content": [{"text": "This is how list could be created in Java", "type": "SimpleText"}]
        }, {
            "lang": "java",
            "maxLineLength": 57,
            "tokens": [{"type": "keyword", "content": "public"}, " ", {
                "type": "keyword",
                "content": "double"
            }, " ", {"type": "function", "content": "calculate"}, {
                "type": "punctuation",
                "content": "("
            }, "List", {"type": "operator", "content": "\u003c"}, "Transaction", {
                "type": "operator",
                "content": "\u003e"
            }, " transactions", {"type": "punctuation", "content": ")"}, " ", {
                "type": "punctuation",
                "content": "{"
            }, "\n    ", {"type": "keyword", "content": "return"}, " ", {
                "type": "number",
                "content": "0"
            }, {"type": "punctuation", "content": ";"}, "\n", {"type": "punctuation", "content": "}"}],
            "type": "Snippet"
        }]
    }, {
        "title": "Input Encapsulation",
        "id": "input-encapsulation",
        "type": "Section",
        "content": [{
            "type": "Paragraph",
            "content": [{"text": "Lets create ", "type": "SimpleText"}, {
                "type": "Emphasis",
                "content": [{"text": "input", "type": "SimpleText"}]
            }, {"text": " data required for a test.", "type": "SimpleText"}]
        }, {
            "lang": "java",
            "maxLineLength": 70,
            "tokens": [{"type": "annotation", "content": "@Test"}, "\n", {
                "type": "keyword",
                "content": "public"
            }, " ", {"type": "keyword", "content": "void"}, " ", {
                "type": "function",
                "content": "marginShouldBeZeroIfNoLotsSet"
            }, {"type": "punctuation", "content": "("}, {
                "type": "punctuation",
                "content": ")"
            }, " ", {"type": "punctuation", "content": "{"}, "\n    Transaction t1 ", {
                "type": "operator",
                "content": "\u003d"
            }, " ", {"type": "keyword", "content": "new"}, " ", {
                "type": "class-name",
                "content": ["Transaction"]
            }, {"type": "punctuation", "content": "("}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t1", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setSymbol"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "string", "content": "\""}, "SYM", {
                "type": "punctuation",
                "content": "."
            }, "B", {"type": "string", "content": "\""}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t1", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setLot"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "number", "content": "0"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t1", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setPrice"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "number", "content": "8"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n\n    Transaction t2 ", {
                "type": "operator",
                "content": "\u003d"
            }, " ", {"type": "keyword", "content": "new"}, " ", {
                "type": "class-name",
                "content": ["Transaction"]
            }, {"type": "punctuation", "content": "("}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t1", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setSymbol"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "string", "content": "\""}, "SYM", {
                "type": "punctuation",
                "content": "."
            }, "C", {"type": "string", "content": "\""}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t1", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setLot"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "number", "content": "0"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t1", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setPrice"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "number", "content": "19"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n\n    ", {
                "type": "function",
                "content": "assertEquals"
            }, {"type": "punctuation", "content": "("}, {
                "type": "number",
                "content": "0"
            }, {"type": "punctuation", "content": ","}, " marginCalculator", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "calculate"}, {
                "type": "punctuation",
                "content": "("
            }, "Arrays", {"type": "punctuation", "content": "."}, {
                "type": "function",
                "content": "asList"
            }, {"type": "punctuation", "content": "("}, "t1", {
                "type": "punctuation",
                "content": ","
            }, " t2", {"type": "punctuation", "content": ")"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ","}, "\n            ", {
                "type": "number",
                "content": "0.0000001"
            }, {"type": "punctuation", "content": ")"}, {
                "type": "punctuation",
                "content": ";"
            }, "\n", {"type": "punctuation", "content": "}"}],
            "type": "Snippet"
        }, {
            "type": "Paragraph",
            "content": [{
                "text": "What will happen to this test when we refactor ",
                "type": "SimpleText"
            }, {
                "code": "Transaction",
                "type": "InlinedCode"
            }, {
                "text": " class? Setters can be replaced with constructor or a ",
                "type": "SimpleText"
            }, {
                "type": "Emphasis",
                "content": [{"text": "Builder", "type": "SimpleText"}]
            }, {"text": " pattern.", "type": "SimpleText"}]
        }, {
            "type": "Paragraph",
            "content": [{
                "text": "Our test is not about how to create ",
                "type": "SimpleText"
            }, {"code": "Transaction", "type": "InlinedCode"}, {
                "text": " instances. It is a about ",
                "type": "SimpleText"
            }, {
                "type": "StrongEmphasis",
                "content": [{"text": "logic of margin calculation", "type": "SimpleText"}]
            }, {
                "text": ".",
                "type": "SimpleText"
            }, {"type": "SoftLineBreak"}, {
                "text": "Our test must survive refactorings.",
                "type": "SimpleText"
            }, {"type": "SoftLineBreak"}, {
                "text": "In order to survive refactoring a ",
                "type": "SimpleText"
            }, {
                "type": "Emphasis",
                "content": [{"text": "Test", "type": "SimpleText"}]
            }, {"text": " must limit its exposure to implementation details.", "type": "SimpleText"}]
        }, {
            "type": "Paragraph",
            "content": [{"text": "Lets encapsulate ", "type": "SimpleText"}, {
                "code": "Transaction",
                "type": "InlinedCode"
            }, {"text": " creation", "type": "SimpleText"}]
        }, {
            "lang": "java",
            "maxLineLength": 87,
            "tokens": [{"type": "keyword", "content": "private"}, " ", {
                "type": "keyword",
                "content": "static"
            }, " Transaction ", {"type": "function", "content": "createTransaction"}, {
                "type": "punctuation",
                "content": "("
            }, "String symbol", {"type": "punctuation", "content": ","}, " ", {
                "type": "keyword",
                "content": "double"
            }, " lot", {"type": "punctuation", "content": ","}, " ", {
                "type": "keyword",
                "content": "double"
            }, " price", {"type": "punctuation", "content": ")"}, " ", {
                "type": "punctuation",
                "content": "{"
            }, "\n    Transaction t ", {"type": "operator", "content": "\u003d"}, " ", {
                "type": "keyword",
                "content": "new"
            }, " ", {"type": "class-name", "content": ["Transaction"]}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "punctuation", "content": ")"}, {
                "type": "punctuation",
                "content": ";"
            }, "\n    t", {"type": "punctuation", "content": "."}, {
                "type": "function",
                "content": "setSymbol"
            }, {"type": "punctuation", "content": "("}, "symbol", {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    t", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "setLot"}, {
                "type": "punctuation",
                "content": "("
            }, "lot", {"type": "punctuation", "content": ")"}, {
                "type": "punctuation",
                "content": ";"
            }, "\n    t", {"type": "punctuation", "content": "."}, {
                "type": "function",
                "content": "setPrice"
            }, {"type": "punctuation", "content": "("}, "price", {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n\n    ", {
                "type": "keyword",
                "content": "return"
            }, " t", {"type": "punctuation", "content": ";"}, "\n", {"type": "punctuation", "content": "}"}],
            "type": "Snippet"
        }, {
            "type": "Paragraph",
            "content": [{"text": "If ", "type": "SimpleText"}, {
                "code": "Transaction",
                "type": "InlinedCode"
            }, {
                "text": " implementation details change we update one place and not every test that depends",
                "type": "SimpleText"
            }, {"type": "SoftLineBreak"}, {"text": "on this core domain object.", "type": "SimpleText"}]
        }, {
            "lang": "java",
            "maxLineLength": 70,
            "tokens": [{"type": "annotation", "content": "@Test"}, "\n", {
                "type": "keyword",
                "content": "public"
            }, " ", {"type": "keyword", "content": "void"}, " ", {
                "type": "function",
                "content": "marginShouldBeZeroIfNoLotsSet"
            }, {"type": "punctuation", "content": "("}, {
                "type": "punctuation",
                "content": ")"
            }, " ", {"type": "punctuation", "content": "{"}, "\n    Transaction t1 ", {
                "type": "operator",
                "content": "\u003d"
            }, " ", {"type": "function", "content": "createTransaction"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "string", "content": "\""}, "SYM", {
                "type": "punctuation",
                "content": "."
            }, "B", {"type": "string", "content": "\""}, {
                "type": "punctuation",
                "content": ","
            }, " ", {"type": "number", "content": "0"}, {
                "type": "punctuation",
                "content": ","
            }, " ", {"type": "number", "content": "8"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n    Transaction t2 ", {
                "type": "operator",
                "content": "\u003d"
            }, " ", {"type": "function", "content": "createTransaction"}, {
                "type": "punctuation",
                "content": "("
            }, {"type": "string", "content": "\""}, "SYM", {
                "type": "punctuation",
                "content": "."
            }, "C", {"type": "string", "content": "\""}, {
                "type": "punctuation",
                "content": ","
            }, " ", {"type": "number", "content": "0"}, {
                "type": "punctuation",
                "content": ","
            }, " ", {"type": "number", "content": "19"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ";"}, "\n\n    ", {
                "type": "function",
                "content": "assertEquals"
            }, {"type": "punctuation", "content": "("}, {
                "type": "number",
                "content": "0"
            }, {"type": "punctuation", "content": ","}, " marginCalculator", {
                "type": "punctuation",
                "content": "."
            }, {"type": "function", "content": "calculate"}, {
                "type": "punctuation",
                "content": "("
            }, "Arrays", {"type": "punctuation", "content": "."}, {
                "type": "function",
                "content": "asList"
            }, {"type": "punctuation", "content": "("}, "t1", {
                "type": "punctuation",
                "content": ","
            }, " t2", {"type": "punctuation", "content": ")"}, {
                "type": "punctuation",
                "content": ")"
            }, {"type": "punctuation", "content": ","}, "\n            ", {
                "type": "number",
                "content": "0.0000001"
            }, {"type": "punctuation", "content": ")"}, {
                "type": "punctuation",
                "content": ";"
            }, "\n", {"type": "punctuation", "content": "}"}],
            "type": "Snippet"
        }]
    }, {
        "title": "Table Data",
        "id": "table-data",
        "type": "Section",
        "content": [{
            "type": "Paragraph",
            "content": [{
                "text": "A few problems with the way we implemented ",
                "type": "SimpleText"
            }, {"code": "createTransaction", "type": "InlinedCode"}, {"text": ":", "type": "SimpleText"}]
        }, {
            "bulletMarker": "*",
            "tight": true,
            "type": "BulletList",
            "content": [{
                "type": "ListItem",
                "content": [{
                    "type": "Paragraph",
                    "content": [{"text": "need to specify all the parameters", "type": "SimpleText"}]
                }]
            }, {
                "type": "ListItem",
                "content": [{
                    "type": "Paragraph",
                    "content": [{"text": "no visible parameter names", "type": "SimpleText"}]
                }]
            }]
        }, {
            "tabsContent": [{
                "name": "Groovy",
                "content": [{
                    "type": "Paragraph",
                    "content": [{
                        "text": "include-groovy: com/twosigma/testing/MarginCalculatorWithGroovyTableDataTest.groovy {entry: \"margin should be zero if no lots set\"}",
                        "type": "SimpleText"
                    }]
                }]
            }, {
                "name": "Java",
                "content": [{
                    "lang": "java",
                    "maxLineLength": 84,
                    "tokens": [{"type": "annotation", "content": "@Test"}, "\n", {
                        "type": "keyword",
                        "content": "public"
                    }, " ", {"type": "keyword", "content": "void"}, " ", {
                        "type": "function",
                        "content": "marginShouldBeZeroIfNoLotsSet"
                    }, {"type": "punctuation", "content": "("}, {
                        "type": "punctuation",
                        "content": ")"
                    }, " ", {
                        "type": "punctuation",
                        "content": "{"
                    }, "\n    TableData transactionsData ", {
                        "type": "operator",
                        "content": "\u003d"
                    }, " ", {"type": "function", "content": "header"}, {
                        "type": "punctuation",
                        "content": "("
                    }, {"type": "string", "content": "\""}, "symbol", {
                        "type": "string",
                        "content": "\""
                    }, {"type": "punctuation", "content": ","}, " ", {
                        "type": "string",
                        "content": "\""
                    }, "lot", {"type": "string", "content": "\""}, {
                        "type": "punctuation",
                        "content": ","
                    }, " ", {"type": "string", "content": "\""}, "price", {
                        "type": "string",
                        "content": "\""
                    }, {"type": "punctuation", "content": ")"}, {
                        "type": "punctuation",
                        "content": "."
                    }, {"type": "function", "content": "values"}, {
                        "type": "punctuation",
                        "content": "("
                    }, "\n                                         ", {
                        "type": "string",
                        "content": "\""
                    }, "SYM", {"type": "punctuation", "content": "."}, "B", {
                        "type": "string",
                        "content": "\""
                    }, {"type": "punctuation", "content": ","}, "  ", {
                        "type": "number",
                        "content": "0"
                    }, {"type": "punctuation", "content": ","}, "     ", {
                        "type": "number",
                        "content": "8"
                    }, {
                        "type": "punctuation",
                        "content": ","
                    }, "\n                                         ", {
                        "type": "string",
                        "content": "\""
                    }, "SYM", {"type": "punctuation", "content": "."}, "C", {
                        "type": "string",
                        "content": "\""
                    }, {"type": "punctuation", "content": ","}, "  ", {
                        "type": "number",
                        "content": "0"
                    }, {"type": "punctuation", "content": ","}, "     ", {
                        "type": "number",
                        "content": "19"
                    }, {"type": "punctuation", "content": ")"}, {
                        "type": "punctuation",
                        "content": ";"
                    }, "\n\n    ", {"type": "keyword", "content": "double"}, " margin ", {
                        "type": "operator",
                        "content": "\u003d"
                    }, " marginCalculator", {"type": "punctuation", "content": "."}, {
                        "type": "function",
                        "content": "calculate"
                    }, {"type": "punctuation", "content": "("}, {
                        "type": "function",
                        "content": "createTransaction"
                    }, {"type": "punctuation", "content": "("}, "transactionsData", {
                        "type": "punctuation",
                        "content": ")"
                    }, {"type": "punctuation", "content": ")"}, {
                        "type": "punctuation",
                        "content": ";"
                    }, "\n    ", {"type": "function", "content": "assertEquals"}, {
                        "type": "punctuation",
                        "content": "("
                    }, {"type": "number", "content": "0"}, {
                        "type": "punctuation",
                        "content": ","
                    }, " margin", {"type": "punctuation", "content": ","}, " ", {
                        "type": "number",
                        "content": "0.0000001"
                    }, {"type": "punctuation", "content": ")"}, {
                        "type": "punctuation",
                        "content": ";"
                    }, "\n", {"type": "punctuation", "content": "}"}],
                    "type": "Snippet"
                }]
            }], "type": "Tabs"
        }]
    }],
    "tocItem": {
        "sectionTitle": "Data",
        "pageTitle": "Input Preparation",
        "fileName": "input-preparation",
        "dirName": "data",
        "pageSectionIdTitles": [{
            "title": "Data Injection",
            "id": "data-injection"
        }, {"title": "Input Encapsulation", "id": "input-encapsulation"}, {
            "title": "Table Data",
            "id": "table-data"
        }]
    }
}]}
