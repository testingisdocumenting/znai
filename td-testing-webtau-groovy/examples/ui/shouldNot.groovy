package ui

import static com.twosigma.testing.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.*

scenario("""Executes negated matcher against page element's underlying value""") {
    search.open()
    search.welcomeMessage.shouldNot == 'welcome to boring search'
}
