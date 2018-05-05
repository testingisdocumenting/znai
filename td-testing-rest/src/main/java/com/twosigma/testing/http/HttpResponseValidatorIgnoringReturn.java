package com.twosigma.testing.http;

import com.twosigma.testing.http.datanode.DataNode;

/**
 * @author mykola
 */
public class HttpResponseValidatorIgnoringReturn implements HttpResponseValidatorWithReturn {
    private HttpResponseValidator validator;

    public HttpResponseValidatorIgnoringReturn(HttpResponseValidator validator) {
        this.validator = validator;
    }

    @Override
    public Object validate(HeaderDataNode header, DataNode body) {
        validator.validate(header, body);
        return null;
    }
}
