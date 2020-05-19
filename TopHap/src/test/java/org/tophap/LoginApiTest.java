package org.tophap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.ApiHelper;
import org.tophap.runner.MultipleTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.tophap.ApiHelper.*;
import static org.tophap.ApiHelper.createLoginRequest;

public class LoginApiTest extends MultipleTest {

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private String idToken;

    @Test
    @Order(1)
    void loginFailed() throws IOException {

        HttpPost request = createLoginRequest("qweqweqwe@gmail.com", "asdasd");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
        }
    }

    @Test
    @Order(2)
    void login() throws IOException {

        HttpPost request = createLoginRequest("qualitya2019+ta1@gmail.com", "TopHap");

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
        ApiHelper.addAuthorizationHeader(request, idToken);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            assertNotNull(entity, "response is null");
        }
    }

    @Test
    @Order(4)
    void testApiHelperLogin() throws IOException {

        ApiHelper.login("qualitya2019+ta1@gmail.com", "TopHap");
        assertTrue(idToken.length() > 0);
    }
}
