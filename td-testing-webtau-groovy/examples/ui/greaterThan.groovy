package ui

import static com.twosigma.testing.webtau.WebTauDsl.beGreaterThan
import static com.twosigma.testing.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.*

scenario("greater than matcher") {
    search.open()

    search.submit(query: "search this")
    search.numberOfResults.waitTo beGreaterThan(1L)
}

