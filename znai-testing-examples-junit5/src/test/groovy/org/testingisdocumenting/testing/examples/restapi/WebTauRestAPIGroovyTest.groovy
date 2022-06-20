/*
 * Copyright 2022 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.testing.examples.restapi

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testingisdocumenting.webtau.junit5.WebTau
import org.testingisdocumenting.webtau.server.WebTauServer

import static org.testingisdocumenting.webtau.WebTauDsl.*

@WebTau
class WebTauRestAPIGroovyTest {
    private static WebTauServer fakeServer

    @BeforeAll
    static void createFakeServer() {
        fakeServer = server.fake("weather", server.router()
                .get("/weather") {request -> server.response([
                        temperature: 90,
                        city: "New York"])
                })

        fakeServer.setAsBaseUrl()
    }

    @AfterAll
    static void stopFakeServer() {
        fakeServer.stop()
    }

    @Test
    void weather() {
        http.get("/weather") {
            temperature.shouldBe < 100
        }

        http.doc.capture("weather-example")
    }
}
