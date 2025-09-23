package com.example.todoapp.steps;

import com.example.todoapp.controller.TaskController;
import com.example.todoapp.model.Task;
import com.example.todoapp.repository.TaskRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
public class TaskSteps {
    @Autowired
    private TaskController controller;

    @Autowired
    private TaskRepository repository;

    private Task task;
    private Exception exception;

    @After
    public void cleanup() {
        repository.deleteAll();
    }

    @Given("I have a task with description {string}")
    public void i_have_task(String desc) {
        task = new Task();
        task.setDescription(desc);
        task.setCompleted(false);
    }

    @When("I add the task")
    public void i_add_task() {
        try {
            task = controller.add(task);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I try to add the task")
    public void i_try_add_task() {
        i_add_task();
    }

    @Then("the task is saved successfully")
    public void task_saved() {
        assertNull(exception, "Unexpected exception: " + (exception != null ? exception.getMessage() : ""));
        assertNotNull(task.getId(), "Task ID should not be null after saving");
        assertTrue(repository.existsById(task.getId()), "Task should exist in the repository");
    }

    @Then("an error is thrown")
    public void error_thrown() {
        assertNotNull(exception, "Expected an exception but none was thrown");
        assertInstanceOf(ResponseStatusException.class, exception,
            "Expected ResponseStatusException but got: " + exception.getClass().getSimpleName());
    }
}
