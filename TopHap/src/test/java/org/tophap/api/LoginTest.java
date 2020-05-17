package org.tophap.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.runner.MultipleTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends MultipleTest {

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private String idToken;

    @Test
    @Order(1)
    void loginFailed() throws IOException {

        String body = "{\"email\":\"qweqweqwe@gmail.com\",\"password\":\"asdasd\",\"returnSecureToken\":true}";

        HttpPost request = new HttpPost("https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDsjeiJsU0dHfsaUVCv5pMehUmLyT26OZM");
        request.setEntity(new StringEntity(body));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
        }
    }

    @Test
    @Order(2)
    void login() throws IOException {

        String body = "{\"email\":\"qualitya2019+ta1@gmail.com\",\"password\":\"TopHap\",\"returnSecureToken\":true}";

        HttpPost request = new HttpPost("https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDsjeiJsU0dHfsaUVCv5pMehUmLyT26OZM");
        request.setEntity(new StringEntity(body));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            assertNotNull(entity, "response is null");

            String responsePayload = EntityUtils.toString(entity);
            JSONObject obj = new JSONObject(responsePayload);

            String token = obj.getString("idToken");
            assertTrue(token.length() > 0);

            idToken = token;
        }
    }

    @Test
    @Order(3)
    void tokenAuthorization() throws IOException {

        HttpGet request = new HttpGet("https://staging-api.tophap.com/users/payment/customers");
        request.addHeader("authorization", idToken);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            assertNotNull(entity, "response is null");
        }
    }
}
