package com.twosigma.testing.http

import com.twosigma.testing.data.traceable.CheckLevel
import com.twosigma.testing.http.datanode.DataNodeBuilder
import com.twosigma.testing.http.datanode.DataNodeId
import com.twosigma.utils.JsonUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class HttpValidationResultTest {
    @Test
    void "should capture validation results"() {
        def responseAsMap = [childA: 'valueA', childB: 'valueB', childC: 'valueC']
        def responseAsJson = JsonUtils.serialize(responseAsMap)

        def n = DataNodeBuilder.fromMap(new DataNodeId('body'), responseAsMap)
        n.get('childA').get().updateCheckLevel(CheckLevel.FuzzyFailed)
        n.get('childB').get().updateCheckLevel(CheckLevel.ExplicitPassed)

        def validationResult = new HttpValidationResult('POST', '/test/url', 'http://site/test/url', null,
                new HttpResponse(content: responseAsJson, contentType: 'application/json', statusCode: 200),
                new HeaderDataNode(), n)

        validationResult.toMap().should equal([method: 'POST', url: 'http://site/test/url', responseType: 'application/json',
                                               responseBody: responseAsJson,
                                               responseBodyChecks: [failedPaths: ['childA'], passedPaths:['childB']]])
    }
}
