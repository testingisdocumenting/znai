/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import React from 'react'
import {elementsLibrary} from '../DefaultElementsLibrary'
import {Page} from './Page'

export function pagesDemo(registry) {
    const defaultPage = defaultPageContent('page1')
    const apiPage = defaultPageContent('page2', 'api')
    registry
        .add('default', () => <Page elementsLibrary={elementsLibrary}
                                    docMeta={docMeta()}
                                    {...defaultPage}/>)
        .add('api', () => <Page elementsLibrary={elementsLibrary}
                                docMeta={docMeta()}
                                {...apiPage}/>)
        .add('two sides', () => <Page elementsLibrary={elementsLibrary}
                                      docMeta={docMeta()}
                                      {...twoSidesPageContent()}/>)
        .add('two sides with tabs', () => (
            <PageMaxHeightWrap>
                <Page elementsLibrary={elementsLibrary}
                      docMeta={docMeta()}
                      {...twoSidesWithTabsPageContent()}/>
            </PageMaxHeightWrap>))
}

function PageMaxHeightWrap({children}) {
    return (
        <div style={{height: '100vh'}}>
            {children}
        </div>
    )
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
                        ],
                        "meta": {
                            "rightSide": true,
                        }
                    },
                    {
                        "bulletMarker": "*",
                        "tight": true,
                        "type": "BulletList",
                        "meta": {
                            "rightSide": true,
                        },
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
                        ],
                        "meta": {
                            "rightSide": true,
                        }
                    },
                    {
                        "bulletMarker": "*",
                        "tight": true,
                        "type": "BulletList",
                        "meta": {
                            "rightSide": true,
                        },
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
                    },
                    {
                        "data": {
                            "store": {
                                "book": [
                                    {
                                        "category": "reference",
                                        "author": "Nigel Rees",
                                        "title": "Sayings of the Century",
                                        "price": 8.95
                                    },
                                    {
                                        "category": "fiction",
                                        "author": "Evelyn Waugh",
                                        "title": "Sword of Honour",
                                        "price": 12.99
                                    },
                                    {
                                        "category": "fiction",
                                        "author": "Herman Melville",
                                        "title": "Moby Dick",
                                        "isbn": "0-553-21311-3",
                                        "price": 8.99
                                    },
                                    {
                                        "category": "fiction",
                                        "author": "J. R. R. Tolkien",
                                        "title": "The Lord of the Rings",
                                        "isbn": "0-395-19395-8",
                                        "price": 22.99
                                    }
                                ],
                                "bicycle": {
                                    "color": "red",
                                    "price": 19.95
                                }
                            },
                            "expensive": 10
                        },
                        "paths": [
                            "root.store.book[0].category",
                            "root.store.book[2].category"
                        ],
                        "type": "Json",
                        "meta": {
                            "rightSide": true,
                        }
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
                        "meta": {
                            "rightSide": true,
                        },
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

function twoSidesPageContent() {
    return {
        "type": "Page",
        "content": [
            {
                "title": "Open API example",
                "id": "open-api-example",
                "type": "Section",
                "content": [
                    {
                        "operation": {
                            "id": "null",
                            "method": "get",
                            "path": "/estimates/time/{param}",
                            "summary": "Time Estimates",
                            "tags": [
                                "Estimates"
                            ],
                            "parameters": [
                                {
                                    "name": "start_latitude",
                                    "in": "query",
                                    "type": "number",
                                    "required": true,
                                    "schema": {},
                                    "description": [
                                        {
                                            "type": "Paragraph",
                                            "content": [
                                                {
                                                    "text": "Latitude component of start location.",
                                                    "type": "SimpleText"
                                                }
                                            ]
                                        }
                                    ]
                                },
                                {
                                    "name": "start_longitude",
                                    "in": "query",
                                    "type": "number",
                                    "required": true,
                                    "schema": {},
                                    "description": [
                                        {
                                            "type": "Paragraph",
                                            "content": [
                                                {
                                                    "text": "Longitude component of start location.",
                                                    "type": "SimpleText"
                                                }
                                            ]
                                        }
                                    ]
                                },
                                {
                                    "name": "customer_uuid",
                                    "in": "query",
                                    "type": "string",
                                    "required": false,
                                    "schema": {},
                                    "description": [
                                        {
                                            "type": "Paragraph",
                                            "content": [
                                                {
                                                    "text": "Unique customer identifier to be used for experience customization.",
                                                    "type": "SimpleText"
                                                }
                                            ]
                                        }
                                    ]
                                },
                                {
                                    "name": "product_id",
                                    "in": "query",
                                    "type": "string",
                                    "required": false,
                                    "schema": {},
                                    "description": [
                                        {
                                            "type": "Paragraph",
                                            "content": [
                                                {
                                                    "text": "Unique identifier representing a specific product for a given latitude \u0026 longitude.",
                                                    "type": "SimpleText"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ],
                            "responses": [
                                {
                                    "code": "200",
                                    "schema": {
                                        "type": "array",
                                        "items": {
                                            "properties": {
                                                "image": {
                                                    "description": [
                                                        {
                                                            "type": "Paragraph",
                                                            "content": [
                                                                {
                                                                    "text": "Image URL representing the product.",
                                                                    "type": "SimpleText"
                                                                }
                                                            ]
                                                        }
                                                    ],
                                                    "type": "string"
                                                },
                                                "product_id": {
                                                    "description": [
                                                        {
                                                            "type": "Paragraph",
                                                            "content": [
                                                                {
                                                                    "text": "Unique identifier representing a specific product for a given latitude \u0026 longitude. For example, uberX in San Francisco will have a different product_id than uberX in Los Angeles.",
                                                                    "type": "SimpleText"
                                                                }
                                                            ]
                                                        }
                                                    ],
                                                    "type": "string"
                                                },
                                                "description": {
                                                    "description": [
                                                        {
                                                            "type": "Paragraph",
                                                            "content": [
                                                                {
                                                                    "text": "Description of product.",
                                                                    "type": "SimpleText"
                                                                }
                                                            ]
                                                        }
                                                    ],
                                                    "type": "string"
                                                },
                                                "display_name": {
                                                    "description": [
                                                        {
                                                            "type": "Paragraph",
                                                            "content": [
                                                                {
                                                                    "text": "Display name of product.",
                                                                    "type": "SimpleText"
                                                                }
                                                            ]
                                                        }
                                                    ],
                                                    "type": "string"
                                                },
                                                "capacity": {
                                                    "description": [
                                                        {
                                                            "type": "Paragraph",
                                                            "content": [
                                                                {
                                                                    "text": "Capacity of product. For example, 4 people.",
                                                                    "type": "SimpleText"
                                                                }
                                                            ]
                                                        }
                                                    ],
                                                    "type": "string"
                                                }
                                            }
                                        }
                                    },
                                    "description": [
                                        {
                                            "type": "Paragraph",
                                            "content": [
                                                {
                                                    "text": "An array of products",
                                                    "type": "SimpleText"
                                                }
                                            ]
                                        }
                                    ]
                                },
                                {
                                    "code": "default",
                                    "schema": {
                                        "properties": {
                                            "code": {
                                                "format": "int32",
                                                "type": "integer"
                                            },
                                            "message": {
                                                "type": "string"
                                            },
                                            "fields": {
                                                "type": "string"
                                            }
                                        }
                                    },
                                    "description": [
                                        {
                                            "type": "Paragraph",
                                            "content": [
                                                {
                                                    "text": "Unexpected error",
                                                    "type": "SimpleText"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ],
                            "description": []
                        },
                        "type": "OpenApiOperation"
                    },
                    {
                        "title": "Response",
                        "meta": {
                            "rightSide": true,
                            "type": "Meta"
                        },
                        "data": {
                            "prices": [
                                {
                                    "localized_display_name": "POOL",
                                    "distance": 6.17,
                                    "display_name": "POOL",
                                    "product_id": "26546650-e557-4a7b-86e7-6a3942445247",
                                    "high_estimate": 15,
                                    "low_estimate": 13,
                                    "duration": 1080,

                                    "estimate": "$13-14",
                                    "currency_code": "USD"
                                },
                                {
                                    "localized_display_name": "uberX",
                                    "distance": 6.17,
                                    "display_name": "uberX",
                                    "product_id": "a1111c8c-c720-46c3-8534-2fcdd730040d",
                                    "high_estimate": 17,
                                    "low_estimate": 13,
                                    "duration": 1080,
                                    "estimate": "$13-17",
                                    "currency_code": "USD"
                                },
                                {
                                    "localized_display_name": "uberXL",
                                    "distance": 6.17,
                                    "display_name": "uberXL",
                                    "product_id": "821415d8-3bd5-4e27-9604-194e4359a449",
                                    "high_estimate": 26,
                                    "low_estimate": 20,
                                    "duration": 1080,
                                    "estimate": "$20-26",
                                    "currency_code": "USD"
                                },
                                {
                                    "localized_display_name": "SELECT",
                                    "distance": 6.17,
                                    "display_name": "SELECT",
                                    "product_id": "57c0ff4e-1493-4ef9-a4df-6b961525cf92",
                                    "high_estimate": 38,
                                    "low_estimate": 30,
                                    "duration": 1080,
                                    "estimate": "$30-38",
                                    "currency_code": "USD"
                                }
                            ]
                        },
                        "paths": [],
                        "type": "Json"
                    }
                ]
            }
        ],
        "lastModifiedTime": 1527373716000,
        "tocItem": {
            "sectionTitle": "Layout",
            "pageTitle": "Two Sides Pages",
            "pageMeta": {
                "type": [
                    "two-sides"
                ]
            },
            "fileName": "two-sides-pages",
            "dirName": "layout",
            "pageSectionIdTitles": [
                {
                    "title": "Setup",
                    "id": "setup"
                },
                {
                    "title": "Details Side",
                    "id": "details-side"
                },
                {
                    "title": "Open API example",
                    "id": "open-api-example"
                }
            ]
        }
    }
}

function twoSidesWithTabsPageContent() {
    return {
        "type": "Page",
        "content": [
            {
                "title": "Primary Use Case",
                "id": "primary-use-case",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Imagine your product supports multiple ways of achieving a final result:",
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
                                                "text": "Web",
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
                                                "text": "Java",
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
                                                "text": "Python",
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
                                                "text": "etc",
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
                                "text": "Provide a selection inside your documentation if your user needs only one to accomplish her goals.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Her selection will be remembered and will provide streamlined experience.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Do not: use tabs as navigation experience.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "If your user needs both ",
                                "type": "SimpleText"
                            },
                            {
                                "code": "Java",
                                "type": "InlinedCode"
                            },
                            {
                                "text": " and ",
                                "type": "SimpleText"
                            },
                            {
                                "code": "Python",
                                "type": "InlinedCode"
                            },
                            {
                                "text": " executed one by one you need to show steps one after another in proper order.",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "title": "Definition",
                "id": "definition",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "To define multiple tabs we use fenced code block",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "lang": "",
                        "tokens": [
                            {
                                "type": "text",
                                "content": "```tabs\nJavaScript: :include-file: snippets/file-name.js\nJava: :include-file: snippets/WideCode.java\nCpp: :include-cpp: snippets/simple.cpp {entry: \u0027main\u0027, bodyOnly: true}\n```\n"
                            }
                        ],
                        "lineNumber": "",
                        "type": "Snippet"
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "This will generate a multi tab widget with an include plugin content per tab",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "rightSide": true,
                        "type": "Meta"
                    },
                    {
                        "tabsContent": [
                            {
                                "name": "JavaScript",
                                "content": [
                                    {
                                        "lang": "javascript",
                                        "tokens": [
                                            {
                                                "type": "keyword",
                                                "content": "class"
                                            },
                                            " ",
                                            {
                                                "type": "class-name",
                                                "content": [
                                                    "JsClass"
                                                ]
                                            },
                                            " ",
                                            {
                                                "type": "punctuation",
                                                "content": "{"
                                            },
                                            "\n    ",
                                            {
                                                "type": "function",
                                                "content": "constructor"
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": "("
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": ")"
                                            },
                                            " ",
                                            {
                                                "type": "punctuation",
                                                "content": "{"
                                            },
                                            "\n    ",
                                            {
                                                "type": "punctuation",
                                                "content": "}"
                                            },
                                            "\n",
                                            {
                                                "type": "punctuation",
                                                "content": "}"
                                            },
                                            "\n\n",
                                            {
                                                "type": "keyword",
                                                "content": "export"
                                            },
                                            " ",
                                            {
                                                "type": "keyword",
                                                "content": "default"
                                            },
                                            " JsClass"
                                        ],
                                        "type": "Snippet"
                                    }
                                ]
                            },
                            {
                                "name": "Java",
                                "content": [
                                    {
                                        "lang": "java",
                                        "tokens": [
                                            {
                                                "type": "keyword",
                                                "content": "class"
                                            },
                                            " ",
                                            {
                                                "type": "class-name",
                                                "content": [
                                                    "InternationalPriceService"
                                                ]
                                            },
                                            " ",
                                            {
                                                "type": "keyword",
                                                "content": "implements"
                                            },
                                            " ",
                                            {
                                                "type": "class-name",
                                                "content": [
                                                    "PriceService"
                                                ]
                                            },
                                            " ",
                                            {
                                                "type": "punctuation",
                                                "content": "{"
                                            },
                                            "\n    ",
                                            {
                                                "type": "keyword",
                                                "content": "private"
                                            },
                                            " ",
                                            {
                                                "type": "keyword",
                                                "content": "static"
                                            },
                                            " ",
                                            {
                                                "type": "keyword",
                                                "content": "void"
                                            },
                                            " LongJavaInterfaceNameWithSuperFactory ",
                                            {
                                                "type": "function",
                                                "content": "createMegaAbstractFactory"
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": "("
                                            },
                                            {
                                                "type": "keyword",
                                                "content": "final"
                                            },
                                            " ExchangeCalendarLongerThanLife calendar",
                                            {
                                                "type": "punctuation",
                                                "content": ")"
                                            },
                                            " ",
                                            {
                                                "type": "punctuation",
                                                "content": "{"
                                            },
                                            "\n        ",
                                            {
                                                "type": "punctuation",
                                                "content": "."
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": "."
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": "."
                                            },
                                            "\n    ",
                                            {
                                                "type": "punctuation",
                                                "content": "}"
                                            },
                                            "\n",
                                            {
                                                "type": "punctuation",
                                                "content": "}"
                                            }
                                        ],
                                        "wide": true,
                                        "type": "Snippet"
                                    }
                                ]
                            },
                            {
                                "name": "Cpp",
                                "content": [
                                    {
                                        "lang": "cpp",
                                        "tokens": [
                                            {
                                                "type": "keyword",
                                                "content": "int"
                                            },
                                            " test ",
                                            {
                                                "type": "operator",
                                                "content": "\u003d"
                                            },
                                            " ",
                                            {
                                                "type": "number",
                                                "content": "2"
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": ";"
                                            },
                                            "\n\n",
                                            {
                                                "type": "comment",
                                                "content": "// comment with **important**"
                                            },
                                            "\n",
                                            {
                                                "type": "comment",
                                                "content": "// information"
                                            },
                                            "\n",
                                            {
                                                "type": "keyword",
                                                "content": "int"
                                            },
                                            " b ",
                                            {
                                                "type": "operator",
                                                "content": "\u003d"
                                            },
                                            " ",
                                            {
                                                "type": "number",
                                                "content": "3"
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": ";"
                                            },
                                            "\n",
                                            {
                                                "type": "keyword",
                                                "content": "int"
                                            },
                                            " d ",
                                            {
                                                "type": "operator",
                                                "content": "\u003d"
                                            },
                                            " ",
                                            {
                                                "type": "number",
                                                "content": "3"
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": ";"
                                            },
                                            "\n\n",
                                            {
                                                "type": "comment",
                                                "content": "/*\n * multi line comment\n * of multi *lines* text\n*/"
                                            },
                                            "\n",
                                            {
                                                "type": "keyword",
                                                "content": "int"
                                            },
                                            " e ",
                                            {
                                                "type": "operator",
                                                "content": "\u003d"
                                            },
                                            " ",
                                            {
                                                "type": "number",
                                                "content": "5"
                                            },
                                            {
                                                "type": "punctuation",
                                                "content": ";"
                                            }
                                        ],
                                        "type": "Snippet"
                                    }
                                ]
                            }
                        ],
                        "type": "Tabs",
                        "meta": {"rightSide": true}
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Selecting a tab will switch all the tabs on every page.",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "title": "Markdown Per Tab",
                "id": "markdown-per-tab",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Any valid markdown can be used in place of a content of each tab.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Typical use case for ",
                                "type": "SimpleText"
                            },
                            {
                                "type": "Emphasis",
                                "content": [
                                    {
                                        "text": "installation instructions",
                                        "type": "SimpleText"
                                    }
                                ]
                            },
                            {
                                "text": ": extract differences per language or environment",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "lang": "",
                        "tokens": [
                            {
                                "type": "text",
                                "content": "````tabs\nJavaScript: \nFirst you need to download WebStorm and then run following using your terminal\n\n```bash\n$ yarn install\n$ yarn start\n```\n    \nJava: \nFirst you need to download Intellij IDEA and then run following using your terminal\n    \n```bash\n$ mvn install\n$ mvn exec:exec\n```\n    \nCpp: \nFirst you need to download CLion and then run following using your terminal\n\n```bash\n$ pwd\n$ whoamis\n```\n````\n"
                            }
                        ],
                        "lineNumber": "",
                        "type": "Snippet"
                    },
                    {
                        "rightSide": true,
                        "type": "Meta"
                    },
                    {
                        "tabsContent": [
                            {
                                "name": "JavaScript",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download WebStorm and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "bash",
                                        "tokens": [
                                            "$ yarn ",
                                            {
                                                "type": "function",
                                                "content": "install"
                                            },
                                            "\n$ yarn start\n"
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    }
                                ]
                            },
                            {
                                "name": "Java",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download Intellij IDEA and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "bash",
                                        "tokens": [
                                            "$ mvn ",
                                            {
                                                "type": "function",
                                                "content": "install"
                                            },
                                            "\n$ mvn exec:exec\n"
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    }
                                ]
                            },
                            {
                                "name": "Cpp",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download CLion and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "bash",
                                        "tokens": [
                                            "$ ",
                                            {
                                                "type": "function",
                                                "content": "pwd"
                                            },
                                            "\n$ whoamis\n"
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    }
                                ]
                            }
                        ],
                        "type": "Tabs",
                        "meta": {"rightSide": true}
                    }
                ]
            },
            {
                "title": "Separate Markdown files",
                "id": "separate-markdown-files",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Consider to extract content to separate markup files if content becomes elaborate.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Use ",
                                "type": "SimpleText"
                            },
                            {
                                "code": "include-markdown",
                                "type": "InlinedCode"
                            },
                            {
                                "text": " plugin to include an external content.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "lang": "",
                        "tokens": [
                            {
                                "type": "text",
                                "content": "```tabs\nJavaScript: :include-markdown: instructions/javascript-install.md\nJava: :include-markdown: instructions/java-install.md\nCpp: :include-markdown: instructions/cpp-install.md\n```\n"
                            }
                        ],
                        "lineNumber": "",
                        "type": "Snippet"
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Each tab will display inlined markdown from specified files.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "rightSide": true,
                        "type": "Meta"
                    },
                    {
                        "tabsContent": [
                            {
                                "name": "JavaScript",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download WebStorm and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "",
                                        "tokens": [
                                            {
                                                "type": "text",
                                                "content": "$ yarn install\n$ yarn start\n"
                                            }
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    }
                                ]
                            },
                            {
                                "name": "Java",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download Intellij IDEA and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "",
                                        "tokens": [
                                            {
                                                "type": "text",
                                                "content": "$ mvn install\n$ mvn exec:exec\n"
                                            }
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    }
                                ]
                            },
                            {
                                "name": "Cpp",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download CLion and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "",
                                        "tokens": [
                                            {
                                                "type": "text",
                                                "content": "$ pwd\n$ whoami\n"
                                            }
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    }
                                ]
                            }
                        ],
                        "type": "Tabs",
                        "meta": {"rightSide": true}
                    }
                ]
            },
            {
                "title": "Attention Signs",
                "id": "attention-signs",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Use extra space(s) in front of ",
                                "type": "SimpleText"
                            },
                            {
                                "code": "Note:",
                                "type": "InlinedCode"
                            },
                            {
                                "text": " or other attention sign keywords to put attention signs inside a tab.",
                                "type": "SimpleText"
                            },
                            {
                                "type": "SoftLineBreak"
                            },
                            {
                                "text": "Remember if you put 4+ spaces your paragraph will become a code snippet",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "lang": "",
                        "tokens": [
                            {
                                "type": "text",
                                "content": "````tabs\nJavaScript: \n\n```bash\n$ yarn install\n$ yarn start\n```\n\n Avoid: committing node_modules\n    \nJava: \nFirst you need to download Intellij IDEA and then run following using your terminal\n    \n```bash\n$ mvn install\n$ mvn exec:exec\n```\n    \n Warning: `mvn` install for the first time may take considerable amount of time\n\nCpp: \nFirst you need to download CLion and then run following using your terminal\n\n```bash\n$ pwd\n$ whoamis\n```\n\n Question: is C++ awesome or what?\n````\n"
                            }
                        ],
                        "lineNumber": "",
                        "type": "Snippet"
                    },
                    {
                        "rightSide": true,
                        "type": "Meta"
                    },
                    {
                        "tabsContent": [
                            {
                                "name": "JavaScript",
                                "content": [
                                    {
                                        "lang": "bash",
                                        "tokens": [
                                            "$ yarn ",
                                            {
                                                "type": "function",
                                                "content": "install"
                                            },
                                            "\n$ yarn start\n"
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    },
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "Do not: commit node_modules to the repository",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "name": "Java",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download Intellij IDEA and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "bash",
                                        "tokens": [
                                            "$ mvn ",
                                            {
                                                "type": "function",
                                                "content": "install"
                                            },
                                            "\n$ mvn exec:exec\n"
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    },
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "Warning: ",
                                                "type": "SimpleText"
                                            },
                                            {
                                                "code": "mvn",
                                                "type": "InlinedCode"
                                            },
                                            {
                                                "text": " install for the first time may take considerable amount of time",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "name": "Cpp",
                                "content": [
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "First you need to download CLion and then run following using your terminal",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    },
                                    {
                                        "lang": "bash",
                                        "tokens": [
                                            "$ ",
                                            {
                                                "type": "function",
                                                "content": "pwd"
                                            },
                                            "\n$ whoamis\n"
                                        ],
                                        "lineNumber": "",
                                        "type": "Snippet"
                                    },
                                    {
                                        "type": "Paragraph",
                                        "content": [
                                            {
                                                "text": "Question: is C++ awesome or what?",
                                                "type": "SimpleText"
                                            }
                                        ]
                                    }
                                ]
                            }
                        ],
                        "type": "Tabs",
                        "meta": {"rightSide": true}
                    }
                ]
            }
        ],
        "lastModifiedTime": 1527473295000,
        "tocItem": {
            "sectionTitle": "Layout",
            "pageTitle": "Tabs",
            "pageMeta": {
                "type": [
                    "two-sides"
                ]
            },
            "fileName": "tabs",
            "dirName": "layout",
            "pageSectionIdTitles": [
                {
                    "title": "Primary Use Case",
                    "id": "primary-use-case"
                },
                {
                    "title": "Definition",
                    "id": "definition"
                },
                {
                    "title": "Markdown Per Tab",
                    "id": "markdown-per-tab"
                },
                {
                    "title": "Separate Markdown files",
                    "id": "separate-markdown-files"
                },
                {
                    "title": "Attention Signs",
                    "id": "attention-signs"
                }
            ]
        }
    }
}