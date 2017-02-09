package com.twosigma.documentation.extensions.table

import org.junit.Test

/**
 * @author mykola
 */
class CsvParserTest {
    @Test
    void "parse simple csv"() {
        def csvData = CsvParser.parse("""Account, Price, Description
#12BGD3, 100, custom table
#12BGD3, 150, chair
#91AGB1, 10, lunch
""")

        assert csvData.toMap() == ["columns": [
                    [
                        "title": "Account"
                    ],
                    [
                        "title": "Price"
                    ],
                    [
                        "title": "Description"
                    ]
            ],
            "data": [
                    [
                            "#12BGD3",
                            "100",
                            'custom table'
                    ],
                    [
                            "#12BGD3",
                            "150",
                            "chair"
                    ],
                    [
                            "#91AGB1",
                            "10",
                            "lunch"
                    ]
            ]
        ]
    }
}
