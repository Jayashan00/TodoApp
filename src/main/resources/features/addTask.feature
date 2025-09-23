Feature: Add a new task
  As a user, I want to add a new task so that I can track my work

  Scenario: Successfully add a task
    Given I have a task with description "Buy groceries"
    When I add the task
    Then the task is saved successfully

  Scenario: Fail to add task with empty description
    Given I have a task with description ""
    When I try to add the task
    Then an error is thrown