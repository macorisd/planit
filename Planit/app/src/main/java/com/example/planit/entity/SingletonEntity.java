package com.example.planit.entity;

import java.util.HashMap;
public class SingletonEntity extends HashMap<String, Object> {

    private static class SingletonHolder {
        private static final SingletonEntity ourInstance = new SingletonEntity();
    }

    public static SingletonEntity getInstance() {
        return SingletonHolder.ourInstance;
    }

    private SingletonEntity() {

    }
}
