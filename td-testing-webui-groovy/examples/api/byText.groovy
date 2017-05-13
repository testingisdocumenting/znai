package api

import static com.twosigma.testing.webui.WebTestGroovyDsl.*

scenario("filter by text") {
    open("/finders-and-filters")
    $("#menu ul li a").get("orders").should == 'orders'
}
