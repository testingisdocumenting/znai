package com.twosigma.testing.http;

import com.twosigma.testing.http.datanode.DataNode;

/**
 * @author mykola
 */
public class HttpResponseValidatorIgnoringReturn implements HttpResponseValidatorWithReturn {
    private HttpResponseValidator validator;

    public HttpResponseValidatorIgnoringReturn(final HttpResponseValidator validator) {
        this.validator = validator;
    }

    @Override
    public Object validate(final HeaderDataNode header, final DataNode body) {
        validator.validate(header, body);
        return null;
    }
}
