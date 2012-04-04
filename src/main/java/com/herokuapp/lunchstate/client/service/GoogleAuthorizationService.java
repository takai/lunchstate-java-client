package com.herokuapp.lunchstate.client.service;


public interface GoogleAuthorizationService {

    String getAuthorizationUrl();

    String loadAccessToken();

    String requestAccessToken(String code);
}
