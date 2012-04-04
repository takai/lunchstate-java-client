package com.herokuapp.lunchstate.client.service.impl;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.common.collect.ImmutableMap;
import com.herokuapp.lunchstate.client.exception.LunchstateRuntimeException;
import com.herokuapp.lunchstate.client.model.StateContainer;
import com.herokuapp.lunchstate.client.service.LunchstateService;

public class LunchstateServiceImpl implements LunchstateService {

    public static class LunchstateUrl extends GenericUrl {
        public LunchstateUrl(String url) {
            super(url);
        }
    }

    public static class LunchstateParameter {

    }

    private static final String LUNCHSTATE_URL_BASE = "https://lunchstate.herokuapp.com/";

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public LunchstateServiceImpl() {
    }

    @Override
    public void createState(String user, String key, boolean value, String token) {
        try {
            LunchstateUrl url = new LunchstateUrl(LUNCHSTATE_URL_BASE + user + "/" + key);
            HttpContent content = new UrlEncodedContent(ImmutableMap.of("access_token", token, "value", value));
            HttpRequest request = createRequestFactory().buildPostRequest(url, content);

            request.execute();
        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

    @Override
    public void deleteState(String user, String key, String token) {
        try {
            LunchstateUrl url = new LunchstateUrl(LUNCHSTATE_URL_BASE + user + "/" + key);
            url.put("access_token", token);
            HttpRequest request = createRequestFactory().buildDeleteRequest(url);

            request.execute();
        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

    @Override
    public StateContainer readState(String user, String key) {
        try {
            LunchstateUrl url = new LunchstateUrl(LUNCHSTATE_URL_BASE + user + "/" + key);
            HttpRequest request = createRequestFactory().buildGetRequest(url);

            return request.execute().parseAs(StateContainer.class);
        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

    @Override
    public void updateState(String user, String key, boolean value, String token) {
        try {
            LunchstateUrl url = new LunchstateUrl(LUNCHSTATE_URL_BASE + user + "/" + key);
            HttpContent content = new UrlEncodedContent(ImmutableMap.of("access_token", token, "value", value));
            HttpRequest request = createRequestFactory().buildPutRequest(url, content);

            request.execute();
        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

    private HttpRequestFactory createRequestFactory() {
        return HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {
                request.addParser(new JsonHttpParser(JSON_FACTORY));
            }
        });
    }

}
