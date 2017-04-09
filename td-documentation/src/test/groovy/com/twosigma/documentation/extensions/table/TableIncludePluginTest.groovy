package com.twosigma.documentation.extensions.table

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

/**
 * @author mykola
 */
class TableIncludePluginTest {
    @Test
    void "should read table from csv by detecting format"() {
        def element = process("test-table.csv")

        assert element == [type: "Table", table: [columns: [[title: "Account"], [title: "Price"], [title: "Description"]],
                                                  data:[["#12BGD3", "100", "custom table with a long attachment"],
                                                        ["#12BGD3", "150", "chair"]]]]
    }

    @Test
    void "should read table from json by detecting format"() {
        def element = process("test-table.json")

        assert element == [type: "Table", table: [columns: [[title: "Account"], [title: "Price"], [title: "Description"]],
                                                  data:[["#12BGD3", 100, "custom table with a long attachment"],
                                                        ["#12BGD3", 150, "chair"]]]]
    }

    private static def process(String fileName, String meta = "") {
        def result = PluginsTestUtils.process(":include-table: $fileName $meta")
        return result.docElements.get(0).toMap()
    }
}
