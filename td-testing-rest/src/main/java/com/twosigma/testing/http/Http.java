package com.twosigma.testing.http;

import com.google.gson.*;
import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.expectation.ExpectationHandler;
import com.twosigma.testing.expectation.ExpectationHandler.Flow;
import com.twosigma.testing.expectation.ExpectationHandlers;
import com.twosigma.testing.http.config.HttpConfigurations;
import com.twosigma.testing.http.datanode.DataNode;
import com.twosigma.testing.http.datanode.DataNodeBuilder;
import com.twosigma.testing.http.datanode.DataNodeId;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class Http {
    private final static Gson gson = createGson();

    public static final Http http = new Http();

    private Http() {
    }

    public <E> E get(final String url, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("GET", url, fullUrl -> get(fullUrl), validator);
    }

    public void get(final String url, HttpResponseValidator validator) {
        get(url, new HttpResponseValidatorIgnoringReturn(validator));
    }

//    public void get(final String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
//
//    }
//

    public <E> E post(final String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("POST", url, fullUrl -> post(fullUrl, requestBody), validator);
    }

    public void post(final String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        executeAndValidateHttpCall("POST", url, fullUrl -> post(fullUrl, requestBody), validator);
    }

    private <E> E executeAndValidateHttpCall(final String requestMethod, final String url, final HttpCall httpCall, HttpResponseValidator validator) {
        return executeAndValidateHttpCall(requestMethod, url, httpCall, new HttpResponseValidatorIgnoringReturn(validator));
    }

    private <E> E executeAndValidateHttpCall(final String requestMethod, final String url, final HttpCall httpCall, HttpResponseValidatorWithReturn validator) {
        final String fullUrl = HttpConfigurations.fullUrl(url);

        try {
            final HttpResponse response = httpCall.execute(fullUrl);
            return validateAndRecord(requestMethod, fullUrl, validator, response);
        } catch (Exception e) {
            throw new RuntimeException("error during http." + requestMethod.toLowerCase() + "(" + fullUrl + ")", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <E> E validateAndRecord(final String requestMethod, final String fullUrl, final HttpResponseValidatorWithReturn validator, final HttpResponse response) {
        final HeaderDataNode header = createHeaderDataNode(response);
        final DataNode body = createBodyDataNode(response);

        HttpValidationResult result = new HttpValidationResult(requestMethod, fullUrl, header, body);

        ExpectationHandler expectationHandler = (actualPath, actualValue, message) -> {
            result.addMismatch(message);
            return Flow.Terminate;
        };

        ExpectationHandlers.add(expectationHandler);
        final Object returnedValue = validator.validate(header, body);
        ExpectationHandlers.remove(expectationHandler);

        HttpTestListeners.afterValidation(result);

        return (E) extractOriginalValue(returnedValue);
    }


    public HttpResponse get(final String fullUrl) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpGet request = new HttpGet(fullUrl);

            return extractHttpResponse(client.execute(request));
        } catch (IOException e) {
            throw new RuntimeException("couldn't get: " + fullUrl, e);
        }
    }

    public HttpResponse post(final String fullUrl, HttpRequestBody requestBody) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpPost post = new HttpPost(fullUrl);

            if (requestBody.isBinary()) {
                throw new UnsupportedOperationException("binary is not supported yet");
            }

            final StringEntity entity = new StringEntity(requestBody.asString());
            entity.setContentType(requestBody.type());

            post.setEntity(entity);

            return extractHttpResponse(client.execute(post));
        } catch (IOException e) {
            throw new RuntimeException("couldn't post: " + fullUrl, e);
        }
    }

    private HttpResponse extractHttpResponse(final CloseableHttpResponse response)
        throws IOException {
        final HttpResponse httpResponse = new HttpResponse();

        httpResponse.setContent(IOUtils.toString(response.getEntity().getContent()));
        httpResponse.setContentType(response.getEntity().getContentType().getValue());
        httpResponse.setStatusCode(response.getStatusLine().getStatusCode());

        return httpResponse;
    }

    private static HeaderDataNode createHeaderDataNode(final HttpResponse response) {
        Map<String, Object> headerData = new LinkedHashMap<>();
        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        return new HeaderDataNode(DataNodeBuilder.fromMap(new DataNodeId("header"), headerData));
    }

    @SuppressWarnings("unchecked")
    private static DataNode createBodyDataNode(final HttpResponse response) {
        try {
            final DataNodeId id = new DataNodeId("body");

            final MapOrList mapOrList = gson.fromJson(response.getContent(), MapOrList.class);

            return mapOrList.list != null ?
                DataNodeBuilder.fromList(id, mapOrList.list):
                DataNodeBuilder.fromMap(id, mapOrList.map);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("error parsing body: " + response.getContent(), e);
        }

    }

    /**
     * Response consist of DataNode and Traceable values but we need to return back a simple value that can be used for
     * regular calculations and to drive test flow
     * @param v value returned from a validation callback
     * @return extracted regular value
     */
    private Object extractOriginalValue(final Object v) {
        // TODO handle maps and list inside
        if (v instanceof DataNode) {
            return ((DataNode) v).get().getValue();
        }

        if (v instanceof TraceableValue) {
            return ((TraceableValue) v).getValue();
        }

        return v;
    }

    private interface HttpCall {
        HttpResponse execute(String fullUrl);
    }

    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(MapOrList.class, new MapOrListDeserializer()).create();
    }

    private static class MapOrList {
        private Map map;
        private List list;
    }

    private static class MapOrListDeserializer implements JsonDeserializer<MapOrList> {
        @Override
        public MapOrList deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final MapOrList result = new MapOrList();
            if (jsonElement.isJsonArray()) {
                result.list = jsonDeserializationContext.deserialize(jsonElement, List.class);
            } else {
                result.map = jsonDeserializationContext.deserialize(jsonElement, Map.class);
            }

            return result;
        }
    }
}
