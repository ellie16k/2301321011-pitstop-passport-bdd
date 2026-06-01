Feature: User Registration and Login
  As a new or existing user of The Pitstop Passport
  I want to register and log into my account
  So that I can access the app features

  # --- REGISTRATION SCENARIOS ---

  Scenario: Successful registration with valid data
    Given the system does not contain a user with email "traveler@pitstop.bg"
    When the user registers with username "racer99", email "traveler@pitstop.bg" and password "Test123"
    Then the registration is successful
    And the system contains a user with email "traveler@pitstop.bg"
    And the user has an empty digital passport created

  Scenario: Registration with an already existing email
    Given user "existingUser" is already registered with email "taken@pitstop.bg"
    When a new user tries to register with email "taken@pitstop.bg" and password "Pass123"
    Then the registration fails
    And the error message is "Email adresat veche e registriran."

  Scenario: Registration with a too short password
    Given the system does not contain a user with email "short@pitstop.bg"
    When the user registers with username "newUser", email "short@pitstop.bg" and password "abc"
    Then the registration fails
    And the error message is "Parolata tryabva da e pone 6 simvola."

  Scenario: Registration with invalid email format
    Given the system does not contain a user with email "invalidemail"
    When the user registers with username "badEmail", email "invalidemail" and password "Test123"
    Then the registration fails
    And the error message is "Nevaliden email adres."

  # --- LOGIN SCENARIOS ---

  Scenario: Successful login with valid credentials
    Given user "racer99" is already registered with email "racer@pitstop.bg" and password "Test123"
    When the user logs in with email "racer@pitstop.bg" and password "Test123"
    Then the login is successful
    And the system returns a valid session token

  Scenario: Login with wrong password
    Given user "racer99" is already registered with email "racer2@pitstop.bg" and password "Test123"
    When the user logs in with email "racer2@pitstop.bg" and password "WrongPass"
    Then the login fails
    And the error message is "Nevalidna parola."

  Scenario: Login with non-existing email
    Given the system does not contain a user with email "ghost@pitstop.bg"
    When the user logs in with email "ghost@pitstop.bg" and password "Test123"
    Then the login fails
    And the error message is "Potrebitelat ne e nameren."
