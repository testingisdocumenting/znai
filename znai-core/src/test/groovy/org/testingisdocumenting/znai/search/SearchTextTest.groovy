/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.search

import org.junit.Test

class SearchTextTest {
    @Test
    void "should remove non readable symbols"() {
        def searchText = new SearchText(SearchScore.STANDARD,
                "CLI renders ANSI colors automatically. \u001B[1mwebtau:\u001B[m000\u001B" +
                        "[1m>\u001B[m http.get(\"https://jsonplaceholder.typicode.com/todos/1\") \u001B[33m>" +
                        " \u001B[34mexecuting HTTP GET" +
                        " \u001B[35mhttps://jsonplaceholder.typicode.com/todos/1\u001B[0m \u001B[32m. " +
                        "\u001B[1mheader.statusCode \u001B[32mequals 200 matches: " +
                        "header.statusCode: actual: 200 <java.lang.Integer> expected: 200 " +
                        "<java.lang.Integer>\u001B[33m (\u001B[32m47ms\u001B[33m)\u001B[0m \u001B[36m{\u001B[0m" +
                        " \u001B[35m\"userId\": 1\u001B[36m,\u001B[0m \u001B[35m\"id\": 1\u001B[36m,\u001B[0m" +
                        " \u001B[35m\"title\": \u001B[32m\"delectus aut autem\"\u001B[36m,\u001B[0m" +
                        " \u001B[35m\"completed\": false\u001B[0m \u001B[36m}\u001B[0m \u001B[32m. " +
                        "\u001B[34mexecuted HTTP GET \u001B[35mhttps://jsonplaceholder.typicode.com/todos/1\u001B[33m" +
                        " (\u001B[32m342ms\u001B[33m)\u001B[0m :include-cli-output: cli/ansi.out " +
                        "\u001B[1mwebtau:\u001B[m000\u001B[1m>\u001B[m" +
                        " http.get(\"https://jsonplaceholder.typicode.com/todos/1\") \u001B[33m> " +
                        "\u001B[34mexecuting HTTP GET \u001B[35mhttps://jsonplaceholder.typicode.com/todos/1\u001B[0m" +
                        " \u001B[32m. \u001B[1mheader.statusCode \u001B[32mequals 200 matches: " +
                        "header.statusCode: actual: 200 <java.lang.Integer> expected: 200 <java.lang.Integer>\u001B[33m " +
                        "(\u001B[32m47ms\u001B[33m)\u001B[0m \u001B[36m{\u001B[0m \u001B[35m\"userId\": " +
                        "1\u001B[36m,\u001B[0m \u001B[35m\"id\": 1\u001B[36m,\u001B[0m \u001B[35m\"title\": " +
                        "\u001B[32m\"delectus aut autem\"\u001B[36m,\u001B[0m \u001B[35m\"completed\": " +
                        "false\u001B[0m \u001B[36m}\u001B[0m \u001B[32m. \u001B[34mexecuted HTTP GET " +
                        "\u001B[35mhttps://jsonplaceholder.typicode.com/todos/1\u001B[33m" +
                        " (\u001B[32m342ms\u001B[33m)\u001B[0m")

        searchText.text.should == "CLI renders ANSI colors automatically. webtau:000> " +
                "http.get(\"https://jsonplaceholder.typicode.com/todos/1\") " +
                "> executing HTTP GET https://jsonplaceholder.typicode.com/todos/1 ." +
                " header.statusCode equals 200 matches: header.statusCode: " +
                "actual: 200 <java.lang.Integer> expected: 200 <java.lang.Integer> (47ms)" +
                " { \"userId\": 1, \"id\": 1, \"title\": \"delectus aut autem\", \"completed\": false } . " +
                "executed HTTP GET https://jsonplaceholder.typicode.com/todos/1 (342ms) :include-cli-output: " +
                "cli/ansi.out webtau:000> http.get(\"https://jsonplaceholder.typicode.com/todos/1\")" +
                " > executing HTTP GET https://jsonplaceholder.typicode.com/todos/1 ." +
                " header.statusCode equals 200 matches: " +
                "header.statusCode: actual: 200 <java.lang.Integer> expected: 200 <java.lang.Integer> (47ms) " +
                "{ \"userId\": 1, \"id\": 1, \"title\": \"delectus aut autem\", \"completed\": false }" +
                " . executed HTTP GET https://jsonplaceholder.typicode.com/todos/1 (342ms)"
    }
}
