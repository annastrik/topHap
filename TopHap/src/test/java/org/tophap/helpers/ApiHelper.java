package org.tophap.helpers;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

public class ApiHelper {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static final String AUTHORIZATION_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDsjeiJsU0dHfsaUVCv5pMehUmLyT26OZM";

    public static String getLoginBody(String email, String password) {
        return String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);
    }

    private static HttpGet createHttpGet(String url) {
        return new HttpGet(url);
    }

    private static HttpPost createHttpPost(String url, String postBody) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(postBody));
        return request;
    }

    private static void addToken(AbstractHttpMessage abstractHttpMessage, String token){
        abstractHttpMessage.addHeader("authorization", token);
    }

    private static HttpGet createHttpGet(String url, String token) {
        HttpGet request = createHttpGet(url);
        addToken(request, token);
        return request;
    }

    private static HttpPost createHttpPost(String url, String postBody, String token) throws UnsupportedEncodingException {
        HttpPost request = createHttpPost(url, postBody);
        addToken(request, token);
        return request;
    }

    public static void doHttpRequest(String url, String postBody, Consumer<CloseableHttpResponse> response) throws IOException {
        doHttpRequest(createHttpPost(url, postBody), response);
    }

    public static void doHttpRequest(String url, Consumer<CloseableHttpResponse> response) throws IOException {
        doHttpRequest(createHttpGet(url), response);
    }

    private static void doHttpRequest(HttpUriRequest request, Consumer<CloseableHttpResponse> response) throws IOException {
        try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {
            response.accept(httpResponse);
        }
    }

    public static void doHttpRequest(String url, Consumer<CloseableHttpResponse> response, String token) throws IOException {
        HttpGet request = createHttpGet(url, token);
        doHttpRequest(request, response);
    }

    public static void doHttpRequest(String url, String postBody, Consumer<CloseableHttpResponse> response, String token) throws IOException {
        HttpPost request = createHttpPost(url, postBody, token);
        doHttpRequest(request, response);
    }

    public static int getHttpRequestStatus(String url) throws IOException {
        int[] status = {0};
        doHttpRequest(url, element -> {
            status[0] = element.getStatusLine().getStatusCode();
        });
        return status[0];
    }

    public static String getToken(String email, String password) throws IOException {
        String[] token = {""};
        doHttpRequest(AUTHORIZATION_URL,
                getLoginBody(email, password),
                element -> {
                    try {
                        token[0] = new JSONObject(EntityUtils.toString(element.getEntity())).getString("idToken");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return token[0];
    }
}