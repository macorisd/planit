package com.example.planit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * SingletonTaskList: Un singleton que mantiene una lista de tareas a lo largo de la app.
 */
public class SingletonTaskList {

    // La instancia única del SingletonTaskList
    private static SingletonTaskList instance;

    // La lista de tareas
    private List<Task> taskList;

    // Constructor privado para evitar instanciación externa
    private SingletonTaskList() {
        taskList = new ArrayList<>();
    }

    // Método para obtener la instancia única del SingletonTaskList
    public static SingletonTaskList getInstance() {
        if (instance == null) {
            instance = new SingletonTaskList();
        }
        return instance;
    }

    // Método para obtener la lista de tareas
    public List<Task> getTaskList() {
        return taskList;
    }

    // Método para añadir una tarea a la lista
    public void addTask(Task task) {
        taskList.add(task);
    }

    // Método para eliminar una tarea de la lista
    public void removeTask(Task task) {
        taskList.remove(task);
    }

    // Método para actualizar una tarea en la lista
    public void updateTask(int index, Task task) {
        if (index >= 0 && index < taskList.size()) {
            taskList.set(index, task);
        }
    }

    // Método para limpiar todas las tareas de la lista
    public void clearTasks() {
        taskList.clear();
    }

    // Método para obtener una tarea por su índice
    public Task getTask(int index) {
        if (index >= 0 && index < taskList.size()) {
            return taskList.get(index);
        }
        return null;
    }
}
