package com.herokuapp.lunchstate.client.service;

import com.herokuapp.lunchstate.client.model.StateContainer;

public interface LunchstateService {

    void createState(String user, String key, boolean value, String token);

    StateContainer readState(String user, String key);

    void updateState(String user, String key, boolean value, String token);

    void deleteState(String user, String key, String token);
}
