Feature: Task Management

  Scenario: Add a valid task
    Given I have a task with description "Buy groceries"
    When I add the task
    Then the task is saved successfully

  Scenario: Cannot add a task without description
    Given I have a task with description ""
    When I try to add the task
    Then an error is thrown
