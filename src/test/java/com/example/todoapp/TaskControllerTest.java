package com.example.todoapp;

import com.example.todoapp.controller.TaskController;
import com.example.todoapp.model.Task;
import com.example.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private TaskController controller;

    @Autowired
    private TaskRepository repository;

    @Test
    public void testAddTask() {
        // Red: Would fail if no save logic
        Task task = new Task();
        task.setDescription("TDD Test Task");
        Task saved = controller.add(task);
        assertNotNull(saved.getId());
        assertEquals("TDD Test Task", saved.getDescription());
        assertFalse(saved.isCompleted()); // Default false
    }

    @Test
    public void testValidateInput() {
        // Red: Would fail if no validation
        Task task = new Task();
        task.setDescription("");
        assertThrows(ResponseStatusException.class, () -> controller.add(task));
    }
}