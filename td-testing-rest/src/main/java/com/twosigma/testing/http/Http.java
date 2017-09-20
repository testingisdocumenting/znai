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
import com.twosigma.testing.reporter.StepReportOptions;
import com.twosigma.testing.reporter.TestStep;
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

    public HttpValidationResult getLastValidationResult() {
        return lastValidationResult.get();
    }

    //    public void get(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
//
//    }
//

    public void captureLastCallInfo(String name) {
    }

    public <E> E post(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("POST", url,
                fullUrl -> post(fullUrl, requestBody),
                requestBody,
                validator);
    }

    public void post(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        executeAndValidateHttpCall("POST", url, fullUrl -> post(fullUrl, requestBody), validator);
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

        step.execute(StepReportOptions.REPORT_ALL);

        return (E) result[0];
    }

    @SuppressWarnings("unchecked")
    private <E> E validateAndRecord(String requestMethod, String url, String fullUrl,
                                    HttpResponseValidatorWithReturn validator,
                                    HttpRequestBody requestBody, HttpResponse response) {
        HeaderDataNode header = createHeaderDataNode(response);
        DataNode body = createBodyDataNode(response);

        HttpValidationResult result = new HttpValidationResult(requestMethod, url, fullUrl, requestBody, header, body);
        lastValidationResult.set(result);

        ExpectationHandler expectationHandler = (actualPath, actualValue, message) -> {
            result.addMismatch(message);
            return Flow.Terminate;
        };

        ExpectationHandlers.add(expectationHandler);

        try {
            Object returnedValue = validator.validate(header, body);
            HttpTestListeners.afterValidation(result);

            return (E) extractOriginalValue(returnedValue);
        } finally {
            ExpectationHandlers.remove(expectationHandler);
        }
    }

    public HttpResponse get(String fullUrl) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(fullUrl);

            return extractHttpResponse(client.execute(request));
        } catch (IOException e) {
            throw new RuntimeException("couldn't get: " + fullUrl, e);
        }
    }

    public HttpResponse post(String fullUrl, HttpRequestBody requestBody) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(fullUrl);

            if (requestBody.isBinary()) {
                throw new UnsupportedOperationException("binary is not supported yet");
            }

            StringEntity entity = new StringEntity(requestBody.asString());
            entity.setContentType(requestBody.type());

            post.setEntity(entity);

            return extractHttpResponse(client.execute(post));
        } catch (IOException e) {
            throw new RuntimeException("couldn't post: " + fullUrl, e);
        }
    }

    private HttpResponse extractHttpResponse(CloseableHttpResponse response)
        throws IOException {
        HttpResponse httpResponse = new HttpResponse();

        httpResponse.setContent(IOUtils.toString(response.getEntity().getContent(), UTF_8));
        httpResponse.setContentType(response.getEntity().getContentType().getValue());
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
