package com.twosigma.testing.webui.expectation

import com.twosigma.testing.webui.FakeWebDriver
import com.twosigma.testing.webui.FakeWebElement
import com.twosigma.testing.webui.page.path.ElementPath
import com.twosigma.testing.webui.page.path.GenericPageElement
import org.junit.Before
import org.junit.Test

/**
 * @author mykola
 */
class PageElementEqualHandlerTest {
    FakeWebDriver driver
    def handler = new PageElementEqualHandler()

    @Before
    void init() {
        driver = new FakeWebDriver()
    }

    @Test
    void "handles page element and any other value"() {
        def pageElement = new GenericPageElement(driver, new ElementPath())

        handler.handle(pageElement, "hello").should == true
        handler.handle(pageElement, 100).should == true

        handler.handle(100, 100).should == false
    }

    @Test
    void "extracts underlying value from element for comparison"() {
        driver.registerFakeElement("fakecss1", FakeWebElement.tagAndText("div", "hello"))
        def pageElement = new GenericPageElement(driver, ElementPath.css("fakecss1"))

        pageElement.should == "hello"
    }
}
