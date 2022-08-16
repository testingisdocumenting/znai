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

package org.testingisdocumenting.znai.charts

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class ChartPluginResultTest {
    @Test
    void "should unpack 'all' breakpoint value into all available values but last"() {
        def params = new PluginParams("barchart", [breakpoint: "all"])
        def result = ChartPluginResult.create(params, "bar", "x, y\n" +
                "test, 10\n" +
                "another, 20\n" +
                "last, 30\n")

        def props = result.docElements[0].toMap()
        props.should == [breakpoint: ["test", "another"],
                         chartType: "bar",
                         data: [["test", 10], ["another", 20], ["last", 30]],
                         labels: ["x", "y"],
                         isTimeSeries: false,
                         type: "EchartGeneric"]
    }

    @Test
    void "unpacking `all` breakpoints should be forbidden for numeric main axis"() {
        def params = new PluginParams("barchart", [breakpoint: "all"])

        code {
            ChartPluginResult.create(params, "bar", "x, y\n" +
                    "15, 10\n" +
                    "25, 20\n")
        } should throwException("<all> breakpoint is not supported for numerical data")
    }

    @Test
    void "should validate and report text breakpoints that don't match available values"() {
        def params = new PluginParams("barchart", [breakpoint: ["test", "hello"]])
        code {
            ChartPluginResult.create(params, "bar", "x, y\n" +
                    "test, 10\n" +
                    "another, 20\n")
        } should throwException("breakpoint value <hello> is not found, available values:\n" +
                "  test\n" +
                "  another")
    }

    @Test
    void "should validate and report numeric breakpoints that are outside of min max values"() {
        def params = new PluginParams("barchart", [breakpoint: [100.12, 300.5]])
        code {
            ChartPluginResult.create(params, "bar", "x, y\n" +
                    "90.343, 10\n" +
                    "100.54454, 20\n")
        } should throwException("breakpoint <300.5> is outside of range [90.343, 100.54454]")
    }
}
