package com.herokuapp.lunchstate.client.model;

import java.util.List;

import com.google.api.client.util.Key;

public class StateContainer {
    @Key
    private List<State> states;

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

}
