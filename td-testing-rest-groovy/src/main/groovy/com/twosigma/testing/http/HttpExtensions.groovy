package com.twosigma.testing.http

import com.twosigma.testing.http.datanode.DataNode
import com.twosigma.testing.http.datanode.GroovyDataNode
import com.twosigma.testing.http.json.JsonRequestBody

/**
 * @author mykola
 */
class HttpExtensions {
    static def get(Http http, String url, Closure validation) {
        return http.get(url, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, Map requestBody, Closure validation) {
        return http.post(url, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    private static HttpResponseValidatorWithReturn closureToHttpResponseValidator(validation) {
        return new HttpResponseValidatorWithReturn() {
            @Override
            def validate(final HeaderDataNode header, final DataNode body) {
                def cloned = validation.clone() as Closure
                cloned.delegate = new ValidatorDelegate(header, body)
                cloned.resolveStrategy = Closure.DELEGATE_FIRST
                return cloned()
            }
        }
    }

    private static class ValidatorDelegate {
        private HeaderDataNode header
        private DataNode body

        ValidatorDelegate(HeaderDataNode header, DataNode body) {
            this.body = body
            this.header = header
        }

        def getProperty(String name) {
            if (name == "header") {
                return new GroovyDataNode(header)
            } else if (name == "body") {
                return new GroovyDataNode(body)
            } else {
                return new GroovyDataNode(body).get(name)
            }
        }
    }
}
