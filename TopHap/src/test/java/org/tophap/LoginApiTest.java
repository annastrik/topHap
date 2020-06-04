package org.tophap;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.helpers.ApiHelper;
import org.tophap.model.api.Login;
import org.tophap.runner.MultipleApiTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginApiTest extends MultipleApiTest {

    private String idToken;

    @Test
    @Order(1)
    void loginFailed() throws IOException {

        ApiHelper.doHttpRequest(Login.AUTHORIZATION_URL,
                Login.getLoginBody("qweqweqwe@gmail.com", "asdasd"),
                element -> {
                    assertEquals(HttpStatus.SC_BAD_REQUEST, element.getStatusLine().getStatusCode());
                });
    }

    @Test
    @Order(2)
    void tokenValidation() throws IOException {

        idToken = Login.getToken("qualitya2019+ta1@gmail.com", "TopHap");
        assertNotEquals("", idToken);
    }

    //Todo test disabled due to bug
    @Disabled
    @Test
    @Order(3)
    void enterPaymentsPageWithToken() throws IOException {

        ApiHelper.doHttpRequest("https://staging-api.tophap.com/users/payment/customers",
                response -> {
                    assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    assertNotNull(response.getEntity(), "response is null");
                },
                idToken);
    }
}