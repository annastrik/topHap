Feature: Search result

  Scenario Outline: Login and city search "<city>"
    Given Go to tophap.com and login
    Then Go to Map page
    When City search "<city>"
    Then First result's city is "<city>"

  Examples:
    | city           |
    | Pleasant Hill  |
    | Daly City      |

  Scenario Outline: Login and zip code search "<zip code>"
    Given Go to tophap.com and login
    Then Go to Map page
    When Zip code search "<zip code>"
    Then First result's zip code is "<zip code>"

  Examples:
    | zip code |
    | 94015    |