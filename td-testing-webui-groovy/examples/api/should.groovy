package api

import static com.twosigma.testing.webui.WebTestGroovyDsl.scenario
import static pages.Pages.*

scenario("""Executes passed matcher against page element's underlying value""") {
    search.open()
    search.welcomeMessage.should == 'welcome to super search'
}

