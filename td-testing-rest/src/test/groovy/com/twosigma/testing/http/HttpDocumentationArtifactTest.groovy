package com.twosigma.testing.http

import com.twosigma.testing.http.datanode.DataNodeBuilder
import com.twosigma.testing.http.datanode.DataNodeId
import org.junit.Test

import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class HttpDocumentationArtifactTest {
    @Test
    void "should capture validation results"() {
        def n = DataNodeBuilder.fromMap(new DataNodeId('body'), [childA: 'valueA', childB: 'valueB'])
        n.get('childA').should equal('valueA')

        def validationResult = new HttpValidationResult('POST', '/test/url', 'http://site/test/url', null,
                new HeaderDataNode(), n)

        def documentationArtifact = new HttpDocumentationArtifact(validationResult)
        documentationArtifact.toMap().should equal([method: 'POST', url: '/test/url',
                                                    body: [childA: 'valueA', childB: 'valueB'],
                                                    paths: ['body.childA']])
    }
}
