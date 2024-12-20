package com.example.planit.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SingletonTaskList: Un singleton que mantiene una lista de tareas a lo largo de la app.
 */
public class SingletonTaskList extends HashMap<String, Object> {

    private static class SingletonHolder {
        private static final SingletonTaskList ourInstance = new SingletonTaskList();
    }

    public static SingletonTaskList getInstance() {
        return SingletonHolder.ourInstance;
    }

    private SingletonTaskList() {

    }
}
