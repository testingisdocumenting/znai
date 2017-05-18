package com.twosigma.testing.data.render

import com.twosigma.testing.data.table.TableData
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author mykola
 */
class TableDataRenderTest {
    @Test
    void "should determine width for each column"() {
        def t = TableData.header(["Column A", "CB", "Column C"])
        t.addRow(["long line of text\nspread across multiple\nlines", "A", "test"])
        t.addRow(["little bit", "CC", "some more\nin two lines"])

        def expected = "\n" +
                ":Column A              |CB|Column C    :\n" +
                ".______________________.__.____________.\n" +
                "|long line of text     |A |test        |\n" +
                "|spread across multiple|  |            |\n" +
                "|lines                 |  |            |\n" +
                ".______________________.__.____________|\n" +
                "|little bit            |CC|some more   |\n" +
                "|                      |  |in two lines|\n" +
                ".______________________.__.____________|\n"

        assertEquals(expected, new TableDataRenderer().render(t))
    }
}
