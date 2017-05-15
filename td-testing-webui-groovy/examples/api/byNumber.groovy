package api

import static com.twosigma.testing.webui.WebTestGroovyDsl.*

scenario("filter by number") {
    open("/finders-and-filters")
    $("#menu ul li a").get(2).should == 'orders'
}
