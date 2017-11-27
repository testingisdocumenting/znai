package com.twosigma.documentation.extensions.table

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

/**
 * @author mykola
 */
class TableIncludePluginTest {
    @Test
    void "should read table from csv by detecting format"() {
        def element = process('test-table.csv')

        assert element == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Price'], [title: 'Description']],
                                                  data:[[[[type:'TestMarkup', markup: '#12BGD3']], [[type:'TestMarkup', markup: '100']],
                                                         [[type:'TestMarkup', markup: 'custom table with a long attachment']]],
                                                        [[[type:'TestMarkup', markup: '#12BGD3']], [[type:'TestMarkup', markup: '150']],
                                                         [[type:'TestMarkup', markup: 'chair']]]]]]
    }

    @Test
    void "should read table from json by detecting format"() {
        def element = process('test-table.json')

        assert element == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Price'], [title: 'Description']],
                                                  data:[[[[type:'TestMarkup', markup: '#12BGD3']], [[type:'TestMarkup', markup: '100.0']],
                                                         [[type:'TestMarkup', markup: 'custom table with a long attachment']]],
                                                        [[[type:'TestMarkup', markup: '#12BGD3']], [[type:'TestMarkup', markup: '150.0']],
                                                         [[type:'TestMarkup', markup: 'chair']]]]]]
    }

    @Test
    void "should filter out columns from json based on case insensitive provided names"() {
        def element = process('test-table.json', '{columns: ["account", "description"]}')

        assert element == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Description']],
                                                  data:[[[[type:'TestMarkup', markup: '#12BGD3']],
                                                         [[type:'TestMarkup', markup: 'custom table with a long attachment']]],
                                                        [[[type:'TestMarkup', markup: '#12BGD3']],
                                                         [[type:'TestMarkup', markup: 'chair']]]]]]
    }

    @Test
    void "should filter out columns from csv based on case insensitive provided names"() {
        def element = process('test-table.csv', '{columns: ["account", "description"]}')

        assert element == [type: 'Table', table: [columns: [[title: 'Account'], [title: 'Description']],
                                                  data:[[[[type:'TestMarkup', markup: '#12BGD3']],
                                                         [[type:'TestMarkup', markup: 'custom table with a long attachment']]],
                                                        [[[type:'TestMarkup', markup: '#12BGD3']],
                                                         [[type:'TestMarkup', markup: 'chair']]]]]]
    }

    private static def process(String fileName, String meta = '') {
        def result = PluginsTestUtils.process(":include-table: $fileName $meta")
        return result.docElements.get(0).toMap()
    }
}
