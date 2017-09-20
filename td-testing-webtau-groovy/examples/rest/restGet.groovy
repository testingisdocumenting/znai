package rest

import static com.twosigma.testing.webtau.WebTauGroovyDsl.*

scenario("simple get") {
    http.get("/weather") {
        temperature.should == 20
    }

    http.doc.capture('weather-sample')
}