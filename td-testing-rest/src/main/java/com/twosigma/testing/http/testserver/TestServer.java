package com.twosigma.testing.http.testserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.twosigma.testing.http.HttpUrl;
import com.twosigma.testing.http.config.HttpConfiguration;
import com.twosigma.testing.http.config.HttpConfigurations;

/**
 * @author mykola
 */
public class TestServer implements HttpConfiguration {
    private int port;
    private Map<String, TestServerResponse> getResponses;
    private Map<String, TestServerResponse> postResponses;
    private Server server;

    public TestServer() {
        getResponses = new HashMap<>();
        postResponses = new HashMap<>();
    }

    public void start(int port) {
        this.port = port;
        server = new Server(port);
        server.setHandler(new RequestHandler());
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpConfigurations.add(this);
    }

    public void stop() {
        try {
            server.stop();
            port = 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpConfigurations.remove(this);
    }

    public void registerGet(String relativeUrl, TestServerResponse response) {
        getResponses.put(relativeUrl, response);
    }

    public void registerPost(String relativeUrl, TestServerResponse response) {
        postResponses.put(relativeUrl, response);
    }

    @Override
    public String fullUrl(final String url) {
        return HttpUrl.concat("http://localhost:" + port, url);
    }

    private class RequestHandler extends AbstractHandler{
        @Override
        public void handle(final String url, final Request baseRequest, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException {

            Map<String, TestServerResponse> responses = findResponses(request);

            final TestServerRequest serverRequest = new TestServerRequest();
            serverRequest.setRequestBody(IOUtils.toString(baseRequest.getReader()));
            serverRequest.setRequestType(baseRequest.getContentType());

            final TestServerResponse testServerResponse = responses.get(url);
            if (testServerResponse == null) {
                response.setStatus(404);
            } else {
                final String responseBody = testServerResponse.responseBody(serverRequest);
                response.setStatus(200);
                response.setContentType(testServerResponse.responseType(serverRequest));
                response.getWriter().println(responseBody != null ? responseBody : "");
            }

            baseRequest.setHandled(true);
        }

        private Map<String, TestServerResponse> findResponses(final HttpServletRequest request) {
            if (request.getMethod().equals("GET")) {
                return getResponses;
            }

            if (request.getMethod().equals("POST")) {
                return postResponses;
            }

            return  Collections.emptyMap();
        }
    }
}
