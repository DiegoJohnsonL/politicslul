Feature: Apply Job Feature

  Scenario Outline: As a postulant i want to postulate a new job.
    Given I am a postulant and look for a new job opportunity
    And The job offer have a stable minimum <salary>
    When I apply for the job
    Then I should be able to see my new application

    Examples:
      | salary |
      | 940    |


  Scenario Outline: As a postulant i try to apply to the same job a second time.
    Given I am a postulant look for job opportunities
    And I have applied to a jobOffer
    When I try to apply to the same job Offer
    Then I should get the error with message <error>

    Examples:
      | error |
      | "El postulante ya postulo a esta oferta de trabajo" |