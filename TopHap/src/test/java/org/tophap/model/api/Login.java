package org.tophap.model.api;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.tophap.helpers.ApiHelper;

import java.io.IOException;

public class Login {

    public static final String AUTHORIZATION_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDsjeiJsU0dHfsaUVCv5pMehUmLyT26OZM";

    public static String getToken(String email, String password) throws IOException {
        String[] token = {""};
        ApiHelper.doHttpRequest(AUTHORIZATION_URL,
                ApiHelper.getLoginBody(email, password),
                response -> {
                    try {
                        token[0] = new JSONObject(EntityUtils.toString(response.getEntity())).getString("idToken");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return token[0];
    }
}