import React from 'react'
import {elementsLibrary} from '../DefaultElementsLibrary'
import {Page} from './Page'

export function pagesDemo(registry) {
    const defaultPage = defaultPageContent('page1')
    const apiPage = defaultPageContent('page2', 'api')

    registry
        .add('default page', <Page elementsLibrary={elementsLibrary}
                                   docMeta={docMeta()}
                                   {...defaultPage}/>)
        .add('api page', <Page elementsLibrary={elementsLibrary}
                               docMeta={docMeta()}
                               {...apiPage}/>)
}

function docMeta() {
    return {
        "title": "Doc Title",
        "type": "Guide"
    }
}

// page should render checks dirname and file name difference
function defaultPageContent(fileName, pageType) {
    return {
        "type": "Page",
        "content": [
            {
                "title": "Stale Documentation",
                "id": "stale-documentation",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Keeping documentation up-to-date is a hard task.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "The further documentation is from the code the more likely it won\u0027t be updated when a feature is added or changed.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Depending on your product and clients, stale documentation effects are:",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "id": "time",
                                                "type": "Icon"
                                            },
                                            {
                                                "text": " users\u0027 time lost",
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
                                                "id": "thumbs-down",
                                                "type": "Icon"
                                            },
                                            {
                                                "text": " reputation damage",
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
                "title": "Part Of Workflow",
                "id": "part-of-workflow",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "As part of your code review you check that there are:",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "no design violations",
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
                                                "text": "no subtle bugs",
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
                                                "text": "tests are updated",
                                                "type": "SimpleText"
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
                                "text": "Now it is time to add to it",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "documentation is updated",
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
                "title": "Maintenance",
                "id": "maintenance",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Keeping documentation up-to-date takes time.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Even non functional changes may require documentation update.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Here are some activities that most likely put your documentation out of sync:",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "moving UI elements around",
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
                                                "text": "renaming REST response fields",
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
                                                "text": "removing redundant command line parameters",
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
                                                "text": "renaming public API classes",
                                                "type": "SimpleText"
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
                                "text": "Our code base already contains a wast amount of data.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Instead of copy-and-paste we leverage artifacts around us.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "examples of how to use API (part of code)",
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
                                                "text": "config files",
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
                                                "text": "test results",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
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
                                                                "text": "Web UI",
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
                                                                "text": "REST",
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
                                                                "text": "CLI",
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
                                                                "text": "business logic",
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
                "title": "Familiar Approach",
                "id": "familiar-approach",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Markup is everywhere",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "StackOverflow",
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
                                                "text": "GitHub",
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
                                                "text": "Jupyter",
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
                                                "text": "Reddit",
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
                                                "text": "Discourse",
                                                "type": "SimpleText"
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
                                "text": "Markup based documentation is widely used as well",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "open source projects (ReactJS, Pandas, Spark)",
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
                                                "text": "technical books (O\u0027Reilly, Manning)",
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
                                                "text": "big companies (Google, FaceBook)",
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
                "title": "Presentations",
                "id": "presentations",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "We build presentations to",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
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
                                                "text": "show new features",
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
                                                "text": "teach a class",
                                                "type": "SimpleText"
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
                                "text": "There is a cost to maintain them.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Instead of building separate slides and keep them up-to-date,",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "this system automatically generates slides from your documentation content.",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ],
        "lastModifiedTime": 1516464705000,
        "tocItem": tocItem(fileName, pageType)
    }
}

function tocItem(fileName, pageType) {
    return {
        "sectionTitle": "Introduction",
        "pageTitle": "Rationale",
        "fileName": fileName,
        "dirName": "introduction",
        "pageMeta": pageType ? {
            "type": [pageType]
        } : {},
        "pageSectionIdTitles": [
            {
                "title": "Stale Documentation",
                "id": "stale-documentation"
            },
            {
                "title": "Part Of Workflow",
                "id": "part-of-workflow"
            },
            {
                "title": "Maintenance",
                "id": "maintenance"
            },
            {
                "title": "Familiar Approach",
                "id": "familiar-approach"
            },
            {
                "title": "Presentations",
                "id": "presentations"
            }
        ]
    }
}
