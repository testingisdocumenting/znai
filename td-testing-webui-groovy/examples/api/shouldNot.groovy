package api

import static com.twosigma.testing.webui.WebTestGroovyDsl.scenario
import static pages.Pages.*

scenario("""Executes negated matcher against page element's underlying value""") {
    search.open()
    search.welcomeMessage.shouldNot == 'welcome to boring search'
}
