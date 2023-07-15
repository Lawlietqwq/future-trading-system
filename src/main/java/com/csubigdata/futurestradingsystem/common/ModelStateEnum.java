package com.csubigdata.futurestradingsystem.common;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ModelStateEnum {

    created("created"),
    started("started"),
    openning("openning"),
    holding("holding"),
    closing("closing"),
    closed("closed");

    private String state;

    @JsonCreator
    ModelStateEnum(String state) {
        this.state = state;
    }

    @JsonValue
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ModelStateEnum{" +
                "state='" + state + '\'' +
                '}';
    }
}
