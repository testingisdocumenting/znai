package com.twosigma.documentation.extensions.table

import org.junit.Test

import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class CsvParserTest {
    @Test
    void "parse simple csv"() {
        def csvData = CsvParser.parse("""Account, Price, "Description"
#12BGD3, 100, "custom, table"
#12BGD3, 150, chair
#91AGB1, 10, lunch
""")

        csvData.toMap().should equal(["columns": [
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
                            100,
                            "custom, table"
                    ],
                    [
                            "#12BGD3",
                            150,
                            "chair"
                    ],
                    [
                            "#91AGB1",
                            10,
                            "lunch"
                    ]
            ]
        ])
    }

    @Test(expected = RuntimeException)
    void "report columns mismatch"() {
        def csvData = CsvParser.parse("""Account, Price, "Description"
#12BGD3, 100, "custom, table"
#12BGD3, 150, chair, extra
#91AGB1, 10, lunch
""")
    }
}
