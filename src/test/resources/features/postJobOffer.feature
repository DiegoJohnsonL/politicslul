Feature: Post Job Offer Feature


  Scenario Outline: As a employeer i want to post a job with at least minimum salary
    Given I am an employeer
    When I try to post new job with a salary of <salary>
    Then I should be able to see my new jobOffer with salary of <salary>
    Examples:
      | salary |
      | 1336   |

#

  Scenario Outline: As a employeer i want to post a job with low minimum salary
    Given I am an employeer
    When I try to post new job with minimum <salary>
    Then I should be able to see <error>
    Examples:
      | salary | error                                     |
      | 910    | "El salario debe ser mayor o igual a 930" |

#