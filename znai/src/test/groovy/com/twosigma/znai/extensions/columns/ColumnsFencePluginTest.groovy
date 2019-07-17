package com.twosigma.znai.extensions.columns

import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

class ColumnsFencePluginTest {
    @Test
    void "should index text from all columns"() {
        def plugin = new ColumnsFencePlugin()
        plugin.process(TestComponentsRegistry.INSTANCE, Paths.get('test.md'), PluginParams.EMPTY,
                """left:text on the left
right:text on the right""")

        plugin.textForSearch().text.should == 'text on the left text on the right'
    }
}
