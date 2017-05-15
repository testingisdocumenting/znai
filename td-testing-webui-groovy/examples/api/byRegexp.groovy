package api

import static com.twosigma.testing.webui.WebTestGroovyDsl.*

scenario("filter by regexp") {
    open("/finders-and-filters")
    $("#menu ul li a").get(~/ord.../).should == 'orders'
}
