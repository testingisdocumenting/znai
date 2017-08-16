package api

import static com.twosigma.testing.webtau.WebTauGroovyDsl.*

scenario("accessing custom config value") {
    open("/finders-and-filters")
    $("#menu ul li a").get(2).should == cfg.userName
}
