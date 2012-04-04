package com.herokuapp.lunchstate.client.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.herokuapp.lunchstate.client.exception.LunchstateRuntimeException;
import com.herokuapp.lunchstate.client.service.GoogleAuthorizationService;

public class GoogleAuthorizationServiceImpl implements GoogleAuthorizationService {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String SECRET_RESOURCE_PATH = "/client_secrets.js";

    private static final String SCOPE_USERINFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";
    private static final String SCOPE_USERINFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";

    private static final String USER_NAME = System.getProperty("user.name");

    private GoogleAuthorizationCodeFlow flow;

    public GoogleAuthorizationServiceImpl() {
        try {
            InputStream clientSecret = this.getClass().getResourceAsStream(SECRET_RESOURCE_PATH);

            GoogleClientSecrets secret = GoogleClientSecrets.load(JSON_FACTORY, clientSecret);
            List<String> scopes = Arrays.asList(SCOPE_USERINFO_PROFILE, SCOPE_USERINFO_EMAIL);

            flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secret, scopes).build();

        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

    @Override
    public String getAuthorizationUrl() {
        return flow.newAuthorizationUrl().setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI).build();
    }

    @Override
    public String loadAccessToken() {
        Credential credential = flow.loadCredential(USER_NAME);
        if (credential != null) {
            return credential.getAccessToken();
        } else {
            return null;
        }
    }

    @Override
    public String requestAccessToken(String code) {
        try {
            GoogleTokenResponse response = flow.newTokenRequest(code)
                                               .setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI)
                                               .execute();
            Credential credential = flow.createAndStoreCredential(response, USER_NAME);

            if (credential != null) {
                return credential.getAccessToken();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

}
