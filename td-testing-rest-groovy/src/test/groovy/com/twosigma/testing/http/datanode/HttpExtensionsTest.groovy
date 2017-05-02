package com.twosigma.testing.http.datanode

import com.twosigma.testing.http.HttpTestListeners
import com.twosigma.testing.http.datacoverage.DataNodeToMapOfValuesConverter
import com.twosigma.testing.http.render.DataNodeRenderer
import com.twosigma.testing.http.testserver.TestServer
import com.twosigma.testing.http.testserver.TestServerJsonResponse
import com.twosigma.testing.http.testserver.TestServerResponseEcho
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.testing.http.Http.http

/**
 * @author mykola
 */
class HttpExtensionsTest {
    static TestServer testServer = new TestServer()

    @BeforeClass
    static void startServer() {
        testServer.start(7823)
        testServer.registerGet("/object", new TestServerJsonResponse("{'id': 10, 'price': 100, 'amount': 30, 'list': [1, 2, 3], 'complexList': [{'k1': 'v1', 'k2': 'v2'}, {'k1': 'v11', 'k2': 'v22'}]}"))
        testServer.registerPost("/echo", new TestServerResponseEcho())

        HttpTestListeners.add({ result ->
            println result.getMismatches().join("\n")
            println DataNodeRenderer.render(result.getBody())
        })
    }

    @AfterClass
    static void stopServer() {
        testServer.stop()
    }

    @Test
    void "use groovy closure as validation"() {
        http.get("/object") {
            price.should == 100
            price.should == 100.0
            price.should == 130
            price.should == 100.001

            list[1].should == 4
        }
    }

    @Test
    void "use table data as expected"() {
        http.get("/object") {
            complexList.should == ["k1"   | "k2"] {
                                  __________________
                                    "v1"  | "v2"
                                    "v11" | "va22" }
        }
    }

    @Test
    void "can return simple value from get"() {
        def id = http.get("/object") {
            return id
        }

        assert id == 10
        assert id.getClass() == Double
    }

    @Test
    void "can return simple value from post"() {
        def id = http.post("/echo", [hello: "world", id: "generated-id"]) {
            hello.should == "world"
            return id
        }

        assert id == "generated-id"
        assert id.getClass() == String
    }
}
