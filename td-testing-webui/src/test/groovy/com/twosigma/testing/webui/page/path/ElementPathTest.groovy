package com.twosigma.testing.webui.page.path

import com.twosigma.testing.webui.page.path.filter.ByNumberElementsFilter
import com.twosigma.testing.webui.page.path.filter.ByTextElementsFilter
import com.twosigma.testing.webui.page.path.finder.ByCssFinder
import org.junit.Test

/**
 * @author mykola
 */
class ElementPathTest {
    @Test
    void "should render full path description"() {
        def path = new ElementPath()
        path.addFinder(new ByCssFinder("#cssid"))
        path.addFilter(new ByTextElementsFilter("about"))
        path.addFilter(new ByNumberElementsFilter(2))

        path.addFinder(new ByCssFinder(".child"))

        assert path.toString() == 'by css #cssid , elements with text about , element number 2 , nested find by css .child'
    }
}
