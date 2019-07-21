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
import TocPanel from './TocPanel'

const docMeta = {
    "title": "MDoc",
    "type": "User Guide",
    "viewOn": {
        "link": "https://github.com/twosigma/TestingIsDocumenting/blob/master/znai-cli/documentation",
        "title": "View On GitHub"
    }
}

export function tocPanelDemo(registry) {
    const selectedTocItem = {dirName: 'snippets', fileName: 'json'}

    registry
        .add('long', () => <TocPanel docMeta={docMeta}
                                     toc={longToc()}
                                     selectedItem={selectedTocItem}/>, '')
        .add('short', () => <TocPanel docMeta={docMeta}
                                      toc={shortToc()}
                                      selectedItem={selectedTocItem}/>, '')
}

function shortToc() {
    return [
        {
            "sectionTitle": "",
            "dirName": "",
            "items": [
                {
                    "sectionTitle": "",
                    "pageTitle": "Index",
                    "pageMeta": {},
                    "fileName": "index",
                    "dirName": "",
                    "pageSectionIdTitles": []
                }
            ]
        },
        {
            "sectionTitle": "Introduction",
            "dirName": "introduction",
            "items": [
                {
                    "sectionTitle": "Introduction",
                    "pageTitle": "Rationale",
                    "pageMeta": {},
                    "fileName": "rationale",
                    "dirName": "introduction",
                },
                {
                    "sectionTitle": "Introduction",
                    "pageTitle": "Example",
                    "pageMeta": {},
                    "fileName": "example",
                    "dirName": "introduction",
                },
                {
                    "sectionTitle": "Introduction",
                    "pageTitle": "Getting Started",
                    "pageMeta": {},
                    "fileName": "getting-started",
                    "dirName": "introduction",
                }
            ]
        },
        {
            "sectionTitle": "Synergy With Testing",
            "dirName": "synergy-with-testing",
            "items": [
                {
                    "sectionTitle": "Synergy With Testing",
                    "pageTitle": "Java",
                    "pageMeta": {},
                    "fileName": "java",
                    "dirName": "synergy-with-testing",
                },
                {
                    "sectionTitle": "Synergy With Testing",
                    "pageTitle": "REST",
                    "pageMeta": {},
                    "fileName": "REST",
                    "dirName": "synergy-with-testing",
                },
                {
                    "sectionTitle": "Synergy With Testing",
                    "pageTitle": "Web UI",
                    "pageMeta": {},
                    "fileName": "web-UI",
                    "dirName": "synergy-with-testing",
                }
            ]
        },
        {
            "sectionTitle": "Configuration",
            "dirName": "configuration",
            "items": [
                {
                    "sectionTitle": "Configuration",
                    "pageTitle": "Basic",
                    "pageMeta": {},
                    "fileName": "basic",
                    "dirName": "configuration",
                }
            ]
        }
    ]
}

function longToc() {
    return [
        {
            "sectionTitle": "",
            "dirName": "",
            "items": [
                {
                    "sectionTitle": "",
                    "pageTitle": "Index",
                    "pageMeta": {},
                    "fileName": "index",
                    "dirName": "",
                    "pageSectionIdTitles": []
                }
            ]
        },
        {
            "sectionTitle": "Introduction",
            "dirName": "introduction",
            "items": [
                {
                    "sectionTitle": "Introduction",
                    "pageTitle": "Rationale",
                    "pageMeta": {},
                    "fileName": "rationale",
                    "dirName": "introduction",
                },
                {
                    "sectionTitle": "Introduction",
                    "pageTitle": "Example",
                    "pageMeta": {},
                    "fileName": "example",
                    "dirName": "introduction",
                },
                {
                    "sectionTitle": "Introduction",
                    "pageTitle": "Getting Started",
                    "pageMeta": {},
                    "fileName": "getting-started",
                    "dirName": "introduction",
                }
            ]
        },
        {
            "sectionTitle": "Flow",
            "dirName": "flow",
            "items": [
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Structure",
                    "pageMeta": {},
                    "fileName": "structure",
                    "dirName": "flow",
                },
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Page Titles",
                    "pageMeta": {
                        "title": [
                            "Page Titles"
                        ]
                    },
                    "fileName": "names",
                    "dirName": "flow",
                },
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Page References",
                    "pageMeta": {},
                    "fileName": "page-references",
                    "dirName": "flow",
                },
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Search",
                    "pageMeta": {},
                    "fileName": "search",
                    "dirName": "flow",
                },
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Footer",
                    "pageMeta": {},
                    "fileName": "footer",
                    "dirName": "flow",
                },
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Presentation",
                    "pageMeta": {},
                    "fileName": "presentation",
                    "dirName": "flow",
                },
                {
                    "sectionTitle": "Flow",
                    "pageTitle": "Testing",
                    "pageMeta": {},
                    "fileName": "testing",
                    "dirName": "flow",
                }
            ]
        },
        {
            "sectionTitle": "Snippets",
            "dirName": "snippets",
            "items": [
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Code Snippets",
                    "pageMeta": {},
                    "fileName": "code-snippets",
                    "dirName": "snippets",
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "External Code Snippets a little bit longer title",
                    "pageMeta": {},
                    "fileName": "external-code-snippets",
                    "dirName": "snippets"
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Java",
                    "pageMeta": {
                        "type": [
                            "api"
                        ]
                    },
                    "fileName": "java",
                    "dirName": "snippets",
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Groovy",
                    "pageMeta": {},
                    "fileName": "groovy",
                    "dirName": "snippets",
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Cpp",
                    "pageMeta": {},
                    "fileName": "cpp",
                    "dirName": "snippets",
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Json",
                    "pageMeta": {},
                    "fileName": "json",
                    "dirName": "snippets",
                    "pageSectionIdTitles": [
                        {
                            "title": "Paths",
                            "id": "paths"
                        },
                        {
                            "title": "Test Results",
                            "id": "test-results"
                        },
                        {
                            "title": "Very long Page Section Test Results Few More",
                            "id": "long-section-1"
                        },
                        {
                            "title": "Another Very long Page Section Test Results Few More",
                            "id": "long-section-2"
                        }
                    ]
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "CLI",
                    "pageMeta": {},
                    "fileName": "CLI",
                    "dirName": "snippets",
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Open API",
                    "pageMeta": {},
                    "fileName": "open-API",
                    "dirName": "snippets",
                },
                {
                    "sectionTitle": "Snippets",
                    "pageTitle": "Math",
                    "pageMeta": {},
                    "fileName": "math",
                    "dirName": "snippets",
                }
            ]
        },
        {
            "sectionTitle": "Layout",
            "dirName": "layout",
            "items": [
                {
                    "sectionTitle": "Layout",
                    "pageTitle": "Attention Signs",
                    "pageMeta": {},
                    "fileName": "attention-signs",
                    "dirName": "layout",
                },
                {
                    "sectionTitle": "Layout",
                    "pageTitle": "Tabs",
                    "pageMeta": {},
                    "fileName": "tabs",
                    "dirName": "layout",
                },
                {
                    "sectionTitle": "Layout",
                    "pageTitle": "Tables",
                    "pageMeta": {},
                    "fileName": "tables",
                    "dirName": "layout",
                },
                {
                    "sectionTitle": "Layout",
                    "pageTitle": "Columns",
                    "pageMeta": {},
                    "fileName": "columns",
                    "dirName": "layout",
                },
                {
                    "sectionTitle": "Layout",
                    "pageTitle": "Templates",
                    "pageMeta": {},
                    "fileName": "templates",
                    "dirName": "layout",
                }
            ]
        },
        {
            "sectionTitle": "Visuals",
            "dirName": "visuals",
            "items": [
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "Smart Bullet Points",
                    "pageMeta": {},
                    "fileName": "smart-bullet-points",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "Icons",
                    "pageMeta": {},
                    "fileName": "icons",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "Charts",
                    "pageMeta": {},
                    "fileName": "charts",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "Images",
                    "pageMeta": {},
                    "fileName": "images",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "Image Annotations",
                    "pageMeta": {},
                    "fileName": "image-annotations",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "SVG",
                    "pageMeta": {},
                    "fileName": "SVG",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "Flow Diagrams",
                    "pageMeta": {},
                    "fileName": "flow-diagrams",
                    "dirName": "visuals",
                },
                {
                    "sectionTitle": "Visuals",
                    "pageTitle": "PlantUml",
                    "pageMeta": {},
                    "fileName": "PlantUml",
                    "dirName": "visuals",
                }
            ]
        },
        {
            "sectionTitle": "Synergy With Testing",
            "dirName": "synergy-with-testing",
            "items": [
                {
                    "sectionTitle": "Synergy With Testing",
                    "pageTitle": "Java",
                    "pageMeta": {},
                    "fileName": "java",
                    "dirName": "synergy-with-testing",
                },
                {
                    "sectionTitle": "Synergy With Testing",
                    "pageTitle": "REST",
                    "pageMeta": {},
                    "fileName": "REST",
                    "dirName": "synergy-with-testing",
                },
                {
                    "sectionTitle": "Synergy With Testing",
                    "pageTitle": "Web UI",
                    "pageMeta": {},
                    "fileName": "web-UI",
                    "dirName": "synergy-with-testing",
                }
            ]
        },
        {
            "sectionTitle": "Configuration",
            "dirName": "configuration",
            "items": [
                {
                    "sectionTitle": "Configuration",
                    "pageTitle": "Basic",
                    "pageMeta": {},
                    "fileName": "basic",
                    "dirName": "configuration",
                }
            ]
        }
    ]
}