package org.tophap;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.helpers.ApiHelper;
import org.tophap.runner.MultipleApiTest;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginApiTest extends MultipleApiTest {

    private String idToken;

    @Test
    @Order(1)
    void loginFailed() throws IOException {

        ApiHelper.doHttpRequest(ApiHelper.AUTHORIZATION_URL,
                ApiHelper.getLoginBody("qweqweqwe@gmail.com", "asdasd"),
                element -> {
                    assertEquals(HttpStatus.SC_BAD_REQUEST, element.getStatusLine().getStatusCode());
                });
    }

    @Test
    @Order(2)
    void tokenValidation() throws IOException {

        idToken = ApiHelper.getToken("qualitya2019+ta1@gmail.com", "TopHap");
        assertNotEquals("", idToken);
    }

    @Test
    @Order(3)
    void enterPaymentsPageWithToken() throws IOException {

        ApiHelper.doHttpRequest("https://staging-api.tophap.com/users/payment/customers",
                element -> {
                    assertEquals(HttpStatus.SC_OK, element.getStatusLine().getStatusCode());
                    assertNotNull(element.getEntity(), "response is null");
                },
                idToken);
    }
}