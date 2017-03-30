package com.twosigma.testing.http;

import com.twosigma.testing.http.datanode.DataNode;

/**
 * @author mykola
 */
public interface HttpResponseValidator {
    void validate(HeaderDataNode header, DataNode body);
}
