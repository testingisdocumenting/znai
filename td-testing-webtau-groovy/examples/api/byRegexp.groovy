package api

import static com.twosigma.testing.webtau.WebTauGroovyDsl.*

scenario("filter by regexp") {
    open("/finders-and-filters")
    $("#menu ul li a").get(~/ord.../).should == 'orders'
}
