package com.twosigma.documentation.website

import com.twosigma.documentation.parser.TestResourceResolver
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class WebSiteExtensionsTest {
    @Test
    void "should let specify extra web resources"() {
        createExtensions([:]).cssResources.size().should == 0

        def extensions = createExtensions([
                cssResources: ['custom.css', 'another.css'],
                jsResources: ['custom.js', 'components.js']])
        extensions.cssResources.path.should == ['custom.css', 'another.css']
        extensions.jsResources.path.should == ['custom.js', 'components.js']
    }

    private static WebSiteExtensions createExtensions(Map definition) {
        return new WebSiteExtensions(new TestResourceResolver(Paths.get('/dummy/root')),  definition)
    }
}
