package org.tophap.model.api;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Login {

    public static final String REQUEST_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDsjeiJsU0dHfsaUVCv5pMehUmLyT26OZM";
    public static final String REQUEST_BODY_FORMAT = "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}";


    public static HttpPost createLoginRequest(String email, String password) throws UnsupportedEncodingException {

        HttpPost request = new HttpPost(REQUEST_URL);
        String body = String.format(REQUEST_BODY_FORMAT, email, password);
        request.setEntity(new StringEntity(body));

        return request;
    }

    public static String login(String email, String password) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = createLoginRequest(email, password);

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String responsePayload = EntityUtils.toString(entity);
        JSONObject obj = new JSONObject(responsePayload);

        return obj.getString("idToken");
    }

    public static void addAuthorizationHeader(HttpRequestBase request, String token) {
        request.addHeader("authorization", token);
    }
}
