package api

import static com.twosigma.testing.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.*

scenario("""Synchronization point with dynamic UI.
Executes negated matcher multiple time until it matches or time runs out.""") {
    search.open()
    search.submit(query: "search this")
    search.numberOfResults.waitToNot == 1
}
