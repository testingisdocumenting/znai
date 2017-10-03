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
import com.twosigma.testing.http.datanode.StructuredDataNode;
import com.twosigma.testing.reporter.StepReportOptions;
import com.twosigma.testing.reporter.TestStep;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.urlValue;
import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author mykola
 */
public class Http {
    private static final Gson gson = createGson();

    public static final Http http = new Http();
    public final HttpDocumentation doc = new HttpDocumentation();

    private ThreadLocal<HttpValidationResult> lastValidationResult = new ThreadLocal<>();

    private Http() {
    }

    public <E> E get(String url, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("GET", url, this::get, null, validator);
    }

    public void get(String url, HttpResponseValidator validator) {
        get(url, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E get(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return get(url + "?" + queryParams.toString(), validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        get(url, queryParams, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("POST", url,
                fullUrl -> post(fullUrl, requestBody),
                requestBody,
                validator);
    }

    public void post(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E put(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("PUT", url,
                fullUrl -> put(fullUrl, requestBody),
                requestBody,
                validator);
    }

    public void put(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E delete(String url, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("DELETE", url, this::delete, null, validator);
    }

    public void delete(String url, HttpResponseValidator validator) {
        delete(url, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public HttpValidationResult getLastValidationResult() {
        return lastValidationResult.get();
    }

    private <E> E executeAndValidateHttpCall(String requestMethod, String url, HttpCall httpCall, HttpResponseValidator validator) {
        return executeAndValidateHttpCall(requestMethod, url, httpCall, null,
                new HttpResponseValidatorIgnoringReturn(validator));
    }

    @SuppressWarnings("unchecked")
    private <E> E executeAndValidateHttpCall(String requestMethod, String url, HttpCall httpCall,
                                             HttpRequestBody requestBody,
                                             HttpResponseValidatorWithReturn validator) {
        String fullUrl = HttpConfigurations.fullUrl(url);

        Object[] result = new Object[1];

        Runnable httpCallRunnable = () -> {
            try {
                HttpResponse response = httpCall.execute(fullUrl);
                result[0] = validateAndRecord(requestMethod, url, fullUrl, validator, requestBody, response);
            } catch (Exception e) {
                throw new RuntimeException("error during http." + requestMethod.toLowerCase() + "(" + fullUrl + ")", e);
            }
        };

        TestStep<E> step = TestStep.create(null, tokenizedMessage(action("executing HTTP " + requestMethod), urlValue(fullUrl)),
                () -> tokenizedMessage(action("executed HTTP " + requestMethod), urlValue(fullUrl)),
                httpCallRunnable);

        try {
            step.execute(StepReportOptions.REPORT_ALL);
        } finally {
            step.addPayload(lastValidationResult.get());
        }

        return (E) result[0];
    }

    @SuppressWarnings("unchecked")
    private <E> E validateAndRecord(String requestMethod, String url, String fullUrl,
                                    HttpResponseValidatorWithReturn validator,
                                    HttpRequestBody requestBody, HttpResponse response) {
        HeaderDataNode header = createHeaderDataNode(response);
        DataNode body = createBodyDataNode(response);

        HttpValidationResult result = new HttpValidationResult(requestMethod, url, fullUrl,
                requestBody, response, header, body);
        lastValidationResult.set(result);

        Object returnedValue = validator.validate(header, body);
        return (E) extractOriginalValue(returnedValue);
    }

    public HttpResponse get(String fullUrl) {
        return requestWithoutBody(new HttpGet(fullUrl));
    }

    public HttpResponse delete(String fullUrl) {
        return requestWithoutBody(new HttpDelete(fullUrl));
    }

    public HttpResponse post(String fullUrl, HttpRequestBody requestBody) {
        return requestWithBody(new HttpPost(fullUrl), requestBody);
    }

    public HttpResponse put(String fullUrl, HttpRequestBody requestBody) {
        return requestWithBody(new HttpPut(fullUrl), requestBody);
    }

    private HttpResponse requestWithoutBody(HttpRequestBase request) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            request.setHeader("Accept", "application/json");

            return extractHttpResponse(client.execute(request));
        } catch (IOException e) {
            throw new RuntimeException("couldn't " + request.getMethod() + ": " + request.getURI(), e);
        }
    }

    private HttpResponse requestWithBody(HttpEntityEnclosingRequestBase request,
                                         HttpRequestBody requestBody) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            request.setHeader("Accept", requestBody.type());
            request.setHeader("Content-type", requestBody.type());

            if (requestBody.isBinary()) {
                throw new UnsupportedOperationException("binary is not supported yet");
            }

            StringEntity entity = new StringEntity(requestBody.asString());
            entity.setContentType(requestBody.type());

            request.setEntity(entity);

            return extractHttpResponse(client.execute(request));
        } catch (IOException e) {
            throw new RuntimeException("couldn't " + request.getMethod() + ": " + request.getURI(), e);
        }
    }

    private HttpResponse extractHttpResponse(CloseableHttpResponse response)
        throws IOException {
        HttpResponse httpResponse = new HttpResponse();

        HttpEntity httpEntity = response.getEntity();
        httpResponse.setContent(httpEntity != null && httpEntity.getContent() != null ?
                IOUtils.toString(httpEntity.getContent(), UTF_8) : "");

        httpResponse.setContentType(httpEntity != null && httpEntity.getContentType() != null ?
                httpEntity.getContentType().getValue() : "");

        httpResponse.setStatusCode(response.getStatusLine().getStatusCode());

        return httpResponse;
    }

    private static HeaderDataNode createHeaderDataNode(HttpResponse response) {
        Map<String, Object> headerData = new LinkedHashMap<>();
        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        return new HeaderDataNode(DataNodeBuilder.fromMap(new DataNodeId("header"), headerData));
    }

    @SuppressWarnings("unchecked")
    private static DataNode createBodyDataNode(HttpResponse response) {
        try {
            DataNodeId id = new DataNodeId("body");
            if (response.getContent().isEmpty()) {
                return new StructuredDataNode(id, new TraceableValue(""));
            }

            if (! response.getContentType().contains("/json")) {
                return new StructuredDataNode(id, new TraceableValue(response.getContent()));
            }

            MapOrList mapOrList = gson.fromJson(response.getContent(), MapOrList.class);

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
    private Object extractOriginalValue(Object v) {
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
        public MapOrList deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            MapOrList result = new MapOrList();
            if (jsonElement.isJsonArray()) {
                result.list = jsonDeserializationContext.deserialize(jsonElement, List.class);
            } else {
                result.map = jsonDeserializationContext.deserialize(jsonElement, Map.class);
            }

            return result;
        }
    }
}
