package com.herokuapp.lunchstate.client.model;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public class State {
    @Key
    private String user;

    @Key
    private String key;

    @Key
    private boolean value;

    @Key("updated_at")
    private DateTime updatedAt;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
