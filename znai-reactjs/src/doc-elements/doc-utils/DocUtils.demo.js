import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'

export function docUtilsDemo(registry) {
    registry
        .add('function with types',
            () => <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[functionWithTypes()]}/>)
        .add('class with methods',
            () => <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[classWithMethods()]}/>)
}

function functionWithTypes() {
    return {
        "desctype": "function",
        "domain": "py",
        "noindex": "False",
        "objtype": "function",
        "type": "DocUtilsDesc",
        "content": [
            {
                "id": "send_message",
                "type": "Anchor"
            },
            {
                "class": "",
                "first": "False",
                "fullname": "send_message",
                "ids": "send_message",
                "module": "True",
                "names": "send_message",
                "type": "DocUtilsDescSignature",
                "content": [
                    {
                        "xmlSpace": "preserve",
                        "type": "DocUtilsDescName",
                        "content": [
                            {
                                "text": "send_message",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "xmlSpace": "preserve",
                        "type": "DocUtilsDescParameterlist",
                        "content": [
                            {
                                "xmlSpace": "preserve",
                                "type": "DocUtilsDescParameter",
                                "content": [
                                    {
                                        "text": "sender",
                                        "type": "SimpleText"
                                    }
                                ]
                            },
                            {
                                "xmlSpace": "preserve",
                                "type": "DocUtilsDescParameter",
                                "content": [
                                    {
                                        "text": "recipient",
                                        "type": "SimpleText"
                                    }
                                ]
                            },
                            {
                                "xmlSpace": "preserve",
                                "type": "DocUtilsDescParameter",
                                "content": [
                                    {
                                        "text": "message_body",
                                        "type": "SimpleText"
                                    }
                                ]
                            },
                            {
                                "xmlSpace": "preserve",
                                "type": "DocUtilsDescOptional",
                                "content": [
                                    {
                                        "xmlSpace": "preserve",
                                        "type": "DocUtilsDescParameter",
                                        "content": [
                                            {
                                                "text": "priority\u003d1",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                "type": "DocUtilsDescContent",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Send a message to a recipient",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "type": "DocUtilsFieldList",
                        "content": [
                            {
                                "type": "DocUtilsField",
                                "content": [
                                    {
                                        "type": "DocUtilsFieldName",
                                        "content": [
                                            {
                                                "text": "Parameters",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "type": "DocUtilsFieldBody",
                                        "content": [
                                            {
                                                "bulletMarker": "*",
                                                "tight": false,
                                                "type": "BulletList",
                                                "content": [
                                                    {
                                                        "type": "ListItem",
                                                        "content": [
                                                            {
                                                                "type": "Paragraph",
                                                                "content": [
                                                                    {
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "sender",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " (",
                                                                        "type": "SimpleText"
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "str",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": ") – ",
                                                                        "type": "SimpleText"
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "text": "The",
                                                                                "type": "SimpleText"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " person ",
                                                                        "type": "SimpleText"
                                                                    },
                                                                    {
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "text": "sending",
                                                                                "type": "SimpleText"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " the message",
                                                                        "type": "SimpleText"
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
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "recipient",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " (",
                                                                        "type": "SimpleText"
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "str",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": ") – The recipient of the message",
                                                                        "type": "SimpleText"
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
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "message_body",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " (",
                                                                        "type": "SimpleText"
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "str",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": ") – The body of the message",
                                                                        "type": "SimpleText"
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
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "priority",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " (",
                                                                        "type": "SimpleText"
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "integer",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": " or ",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "type": "Emphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "None",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": ") – The priority of the message, can be a number 1-5",
                                                                        "type": "SimpleText"
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "type": "DocUtilsField",
                                "content": [
                                    {
                                        "type": "DocUtilsFieldName",
                                        "content": [
                                            {
                                                "text": "Returns",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "type": "DocUtilsFieldBody",
                                        "content": [
                                            {
                                                "type": "Paragraph",
                                                "content": [
                                                    {
                                                        "text": "the message id",
                                                        "type": "SimpleText"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "type": "DocUtilsField",
                                "content": [
                                    {
                                        "type": "DocUtilsFieldName",
                                        "content": [
                                            {
                                                "text": "Return type",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "type": "DocUtilsFieldBody",
                                        "content": [
                                            {
                                                "type": "Paragraph",
                                                "content": [
                                                    {
                                                        "text": "int",
                                                        "type": "SimpleText"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "type": "DocUtilsField",
                                "content": [
                                    {
                                        "type": "DocUtilsFieldName",
                                        "content": [
                                            {
                                                "text": "Raises",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "type": "DocUtilsFieldBody",
                                        "content": [
                                            {
                                                "bulletMarker": "*",
                                                "tight": false,
                                                "type": "BulletList",
                                                "content": [
                                                    {
                                                        "type": "ListItem",
                                                        "content": [
                                                            {
                                                                "type": "Paragraph",
                                                                "content": [
                                                                    {
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "ValueError",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " – if the message_body exceeds 160 characters",
                                                                        "type": "SimpleText"
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
                                                                        "type": "StrongEmphasis",
                                                                        "content": [
                                                                            {
                                                                                "code": "TypeError",
                                                                                "type": "InlinedCode"
                                                                            }
                                                                        ]
                                                                    },
                                                                    {
                                                                        "text": " – if the message_body is not a basestring",
                                                                        "type": "SimpleText"
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "type": "StrongEmphasis",
                                "content": [
                                    {
                                        "text": "example",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "hello world",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ]
    }
}

function classWithMethods() {
    return {
        "desctype": "class",
        "domain": "py",
        "noindex": "False",
        "objtype": "class",
        "type": "DocUtilsDesc",
        "content": [
            {
                "id": "world.HelloWorld",
                "type": "Anchor"
            },
            {
                "class": "",
                "first": "False",
                "fullname": "HelloWorld",
                "ids": "world.HelloWorld",
                "module": "world",
                "names": "world.HelloWorld",
                "type": "DocUtilsDescSignature",
                "content": [
                    {
                        "xmlSpace": "preserve",
                        "type": "DocUtilsDescAnnotation",
                        "content": [
                            {
                                "text": "class ",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "xmlSpace": "preserve",
                        "type": "DocUtilsDescAddname",
                        "content": [
                            {
                                "text": "world.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "xmlSpace": "preserve",
                        "type": "DocUtilsDescName",
                        "content": [
                            {
                                "text": "HelloWorld",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "DocUtilsDescContent",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Simple hello world class",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "desctype": "method",
                        "domain": "py",
                        "noindex": "False",
                        "objtype": "method",
                        "type": "DocUtilsDesc",
                        "content": [
                            {
                                "id": "world.HelloWorld.sayBye",
                                "type": "Anchor"
                            },
                            {
                                "class": "HelloWorld",
                                "first": "False",
                                "fullname": "HelloWorld.sayBye",
                                "ids": "world.HelloWorld.sayBye",
                                "module": "world",
                                "names": "world.HelloWorld.sayBye",
                                "type": "DocUtilsDescSignature",
                                "content": [
                                    {
                                        "xmlSpace": "preserve",
                                        "type": "DocUtilsDescName",
                                        "content": [
                                            {
                                                "text": "sayBye",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "xmlSpace": "preserve",
                                        "type": "DocUtilsDescParameterlist",
                                        "content": [
                                            {
                                                "xmlSpace": "preserve",
                                                "type": "DocUtilsDescParameter",
                                                "content": [
                                                    {
                                                        "text": "name",
                                                        "type": "SimpleText"
                                                    }
                                                ]
                                            },
                                            {
                                                "xmlSpace": "preserve",
                                                "type": "DocUtilsDescParameter",
                                                "content": [
                                                    {
                                                        "text": "title",
                                                        "type": "SimpleText"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "type": "DocUtilsDescContent",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "says bye",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "type": "DocUtilsFieldList",
                                        "content": [
                                            {
                                                "type": "DocUtilsField",
                                                "content": [
                                                    {
                                                        "type": "DocUtilsFieldName",
                                                        "content": [
                                                            {
                                                                "text": "Parameters",
                                                                "type": "SimpleText"
                                                            }
                                                        ]
                                                    },
                                                    {
                                                        "type": "DocUtilsFieldBody",
                                                        "content": [
                                                            {
                                                                "bulletMarker": "*",
                                                                "tight": false,
                                                                "type": "BulletList",
                                                                "content": [
                                                                    {
                                                                        "type": "ListItem",
                                                                        "content": [
                                                                            {
                                                                                "type": "Paragraph",
                                                                                "content": [
                                                                                    {
                                                                                        "type": "StrongEmphasis",
                                                                                        "content": [
                                                                                            {
                                                                                                "code": "name",
                                                                                                "type": "InlinedCode"
                                                                                            }
                                                                                        ]
                                                                                    },
                                                                                    {
                                                                                        "text": " – name of a person to greet",
                                                                                        "type": "SimpleText"
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
                                                                                        "type": "StrongEmphasis",
                                                                                        "content": [
                                                                                            {
                                                                                                "code": "title",
                                                                                                "type": "InlinedCode"
                                                                                            }
                                                                                        ]
                                                                                    },
                                                                                    {
                                                                                        "text": " – title of a person to greet",
                                                                                        "type": "SimpleText"
                                                                                    }
                                                                                ]
                                                                            }
                                                                        ]
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "desctype": "method",
                        "domain": "py",
                        "noindex": "False",
                        "objtype": "method",
                        "type": "DocUtilsDesc",
                        "content": [
                            {
                                "id": "world.HelloWorld.sayHi",
                                "type": "Anchor"
                            },
                            {
                                "class": "HelloWorld",
                                "first": "False",
                                "fullname": "HelloWorld.sayHi",
                                "ids": "world.HelloWorld.sayHi",
                                "module": "world",
                                "names": "world.HelloWorld.sayHi",
                                "type": "DocUtilsDescSignature",
                                "content": [
                                    {
                                        "xmlSpace": "preserve",
                                        "type": "DocUtilsDescName",
                                        "content": [
                                            {
                                                "text": "sayHi",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "xmlSpace": "preserve",
                                        "type": "DocUtilsDescParameterlist",
                                        "content": [
                                            {
                                                "xmlSpace": "preserve",
                                                "type": "DocUtilsDescParameter",
                                                "content": [
                                                    {
                                                        "text": "name",
                                                        "type": "SimpleText"
                                                    }
                                                ]
                                            },
                                            {
                                                "xmlSpace": "preserve",
                                                "type": "DocUtilsDescParameter",
                                                "content": [
                                                    {
                                                        "text": "title",
                                                        "type": "SimpleText"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "type": "DocUtilsDescContent",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "says hi",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "type": "DocUtilsFieldList",
                                        "content": [
                                            {
                                                "type": "DocUtilsField",
                                                "content": [
                                                    {
                                                        "type": "DocUtilsFieldName",
                                                        "content": [
                                                            {
                                                                "text": "Parameters",
                                                                "type": "SimpleText"
                                                            }
                                                        ]
                                                    },
                                                    {
                                                        "type": "DocUtilsFieldBody",
                                                        "content": [
                                                            {
                                                                "bulletMarker": "*",
                                                                "tight": false,
                                                                "type": "BulletList",
                                                                "content": [
                                                                    {
                                                                        "type": "ListItem",
                                                                        "content": [
                                                                            {
                                                                                "type": "Paragraph",
                                                                                "content": [
                                                                                    {
                                                                                        "type": "StrongEmphasis",
                                                                                        "content": [
                                                                                            {
                                                                                                "code": "name",
                                                                                                "type": "InlinedCode"
                                                                                            }
                                                                                        ]
                                                                                    },
                                                                                    {
                                                                                        "text": " – name of a person to greet",
                                                                                        "type": "SimpleText"
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
                                                                                        "type": "StrongEmphasis",
                                                                                        "content": [
                                                                                            {
                                                                                                "code": "title",
                                                                                                "type": "InlinedCode"
                                                                                            }
                                                                                        ]
                                                                                    },
                                                                                    {
                                                                                        "text": " – title of a person to greet",
                                                                                        "type": "SimpleText"
                                                                                    }
                                                                                ]
                                                                            }
                                                                        ]
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }]
    }
}