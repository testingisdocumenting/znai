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

package webtauexamples

import org.testingisdocumenting.webtau.server.WebTauServer

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

WebTauServer employeeServer

scenario("start fake server") {
    def router = server.router()
            .post("/employee") { request -> return server.response([id: "employee-id"])}
            .get("/employee") { request -> return server.response([id: "employee-id", firstName: "FN", lastName: "LN"])}

    employeeServer = server.fake("employee", router)
    employeeServer.setAsBaseUrl()
}

// example
scenario("create and access employee") {
    def id = http.post("/employee", [firstName: 'FN', lastName: 'LN']) {
        id.shouldNot == ""
        return id
    }
    http.doc.capture('employee-post') // capture previous HTTP call into <docDir>/employee-post

    http.get("/employee/$id") {
        firstName.should == "FN"
        lastName.should == "LN"
    }
    http.doc.capture('employee-get') // capture previous HTTP call into <docDir>/employee-get
}
// example

scenario("stop server") {
    employeeServer.stop()
}