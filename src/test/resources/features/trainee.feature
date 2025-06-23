Feature: Trainee Management
  As a gym administrator
  I want to manage trainee profiles
  So that I can keep track of all trainees in the system

  Background:
    Given the system has a test trainee with username "test.trainee"

  Scenario: Get trainee profile by username
    When I request the trainee profile with username "test.trainee"
    Then the response status code should be 200
    And the response should contain the trainee's details

  Scenario: Create a new trainee
    When I create a new trainee with the following details:
      | firstName | lastName |
      | John      | Doe      |
    Then the response status code should be 201
    And the response should contain the generated username and password

  Scenario: Update trainee profile
    When I update the trainee with username "test.trainee" with the following details:
      | firstName | lastName | dateOfBirth | address   | active |
      | John      | Updated  | 1990-01-01  | New York  | true   |
    Then the response status code should be 200
    And the response should contain the updated trainee details

  Scenario: Delete trainee
    When I delete the trainee with username "test.trainee"
    Then the response status code should be 200
    And the trainee should be removed from the system

  Scenario: Get trainee's trainings
    Given the trainee "test.trainee" has training sessions
    When I request the trainings for trainee "test.trainee"
    Then the response status code should be 200
    And the response should contain the trainee's training sessions

  Scenario: Update trainee status
    When I update the status of trainee "test.trainee" to inactive
    Then the response status code should be 200
    And the trainee's status should be updated to inactive