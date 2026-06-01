Feature: GPS Location Check-in
  As a Traveler
  I want to stamp my passport by physically visiting a pitstop
  So that I can collect stamps and unlock rewards

  Background:
    Given the user "moto_fan" is registered and logged in
    And the system has a pitstop "Kamanni Gabi" with coordinates 41.6233, 25.3697
    And the user has no previous stamps

  # --- POSITIVE SCENARIOS ---

  Scenario: Successful check-in within 50 metres
    Given the user is at coordinates 41.6233, 25.3697
    When the user attempts to check in at "Kamanni Gabi"
    Then the check-in is successful
    And a new stamp for "Kamanni Gabi" is added to the passport
    And the stamp count in the passport is 1

  Scenario: Successful check-in at exactly 50 metres
    Given the user is at coordinates 41.6237, 25.3697
    When the user attempts to check in at "Kamanni Gabi"
    Then the check-in is successful
    And the stamp count in the passport is 1

  # --- NEGATIVE SCENARIOS ---

  Scenario: Failed check-in - user is too far away
    Given the user is at coordinates 42.6977, 23.3219
    When the user attempts to check in at "Kamanni Gabi"
    Then the check-in fails
    And the error contains "Too far from location"
    And the stamp count in the passport is 0

  Scenario: Failed check-in - user is 100 metres away
    Given the user is at coordinates 41.6242, 25.3697
    When the user attempts to check in at "Kamanni Gabi"
    Then the check-in fails
    And the error contains "Too far from location"

  Scenario: Failed check-in - stamp already exists for this pitstop
    Given the user is at coordinates 41.6233, 25.3697
    And the user has already checked in at "Kamanni Gabi"
    When the user attempts to check in at "Kamanni Gabi"
    Then the check-in fails
    And the error contains "Veche imash pechat"

  Scenario: Distance calculation between two far apart points
    Given the user is at coordinates 42.1433, 24.7494
    When the system calculates the distance to the pitstop at coordinates 41.6233, 25.3697
    Then the calculated distance is greater than 50 metres
