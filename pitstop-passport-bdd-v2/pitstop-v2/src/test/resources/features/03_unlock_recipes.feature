Feature: Unlock Regional Recipes
  As a Traveler
  I want to unlock exclusive recipes after collecting enough stamps
  So that I get rewarded for exploring new locations

  Background:
    Given the user "chef_traveler" is registered and logged in for recipes
    And the system has a recipe "Dobrudjanski Hlyab" from region "Dobrudzha" requiring 6 stamps

  # --- POSITIVE SCENARIOS ---

  Scenario: Successfully unlock a recipe with exactly enough stamps
    Given the user has 6 stamps in their passport
    When the user attempts to unlock recipe "Dobrudjanski Hlyab"
    Then the recipe is unlocked successfully
    And the recipe is added to the user's unlocked recipes list

  Scenario: Successfully unlock a recipe with more than enough stamps
    Given the user has 10 stamps in their passport
    When the user attempts to unlock recipe "Dobrudjanski Hlyab"
    Then the recipe is unlocked successfully

  Scenario: User views their unlocked recipes list
    Given the user has 6 stamps in their passport
    And the user has already unlocked recipe "Dobrudjanski Hlyab"
    When the user views their unlocked recipes
    Then the list contains the recipe "Dobrudjanski Hlyab"

  # --- NEGATIVE SCENARIOS ---

  Scenario: Failed unlock - not enough stamps
    Given the user has 3 stamps in their passport
    When the user attempts to unlock recipe "Dobrudjanski Hlyab"
    Then the unlock fails
    And the error contains "Nedostatachen broy pechati"

  Scenario: Failed unlock - recipe already unlocked
    Given the user has 6 stamps in their passport
    And the user has already unlocked recipe "Dobrudjanski Hlyab"
    When the user attempts to unlock recipe "Dobrudjanski Hlyab"
    Then the unlock fails
    And the exact error is "Retseptata veche e otkluchena."

  Scenario: Failed unlock - zero stamps
    Given the user has 0 stamps in their passport
    When the user attempts to unlock recipe "Dobrudjanski Hlyab"
    Then the unlock fails
    And the error contains "Nedostatachen broy pechati"
