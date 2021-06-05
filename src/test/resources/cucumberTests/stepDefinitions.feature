Feature: Check tax on car
  Scenario : User enters a valid Reg number and clicks the free check option
    Given Valid Registration value of "LT09YJJ"
    When the website "cartaxcheck.co.uk" is live
    And user enters value of "LT09YJJ"
    And user clicks on the free option
    Then user should be taken to the "cartaxcheck.co.uk/free-car-check" page
    And Number plate "LT09YJJ" Should appear
