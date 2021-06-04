Feature: Check tax on car
  Scenario : User enters a valid Reg number and clicks the free check option
    Given Valid Registration value of LT09YJJ
    When user clicks on free car check
    Then user should be taken to the cartaxcheck.co.uk/free-car-check page
    Then Number plate LT09YJJ Should appear with make Volkswagen
  Scenario: User enters an invalid Reg Number and clicks the free check option
    Given Invalid registration value of TESTING
    When user clicks on free car check
    Then Url stays in cartaxcheck.co.uk
