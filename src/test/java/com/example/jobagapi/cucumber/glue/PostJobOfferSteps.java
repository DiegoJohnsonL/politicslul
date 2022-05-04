package com.example.jobagapi.cucumber.glue;

import com.example.jobagapi.domain.repository.EmployeerRepository;
import com.example.jobagapi.domain.repository.JobOfferRepository;
import com.example.jobagapi.exception.ExceptionResponse;
import com.example.jobagapi.resource.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class PostJobOfferSteps {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EmployeerRepository employeerRepository;
    @Autowired
    private JobOfferRepository jobOfferRepository;

    private Long employeerId;
    private String expectedMessage;
    private Long jobOfferId;

    @Given("I am an employeer")
    public void iAmAnEmployeer() {
        SaveEmployeerResource employeerRequest = (SaveEmployeerResource) new SaveEmployeerResource()
                .setPosicion("")
                .setPassword("")
                .setDocument("")
                .setNumber(1L)
                .setFirstname("")
                .setLastname("")
                .setEmail("eployeer@email.com");
        EmployeerResource employeerResponse = restTemplate
                .postForObject("/api/employeers", employeerRequest, EmployeerResource.class);
        employeerId = employeerResponse.getId();
    }

    @When("I try to post new job with minimum {long}")
    public void iTryToPostNewJobWithMinimumSalary(Long salary) {
        SaveJobOfferResource jobOfferRequest = new SaveJobOfferResource()
                .setSalary(salary)
                .setDescription("")
                .setType("")
                .setTitle("")
                .setDirection("");
        ExceptionResponse response = restTemplate.postForObject("/api/employeers/" + employeerId + "/joboffers", jobOfferRequest, ExceptionResponse.class);
        expectedMessage = response.getMessage();
    }

    @Then("I should be able to see {string}")
    public void iShouldBeAbleToSeeError(String error) {
        assertThat(expectedMessage).isEqualTo(error);
        cleanup();
    }

    @When("I try to post new job with a salary of {long}")
    public void iTryToPostNewJobWithASalaryOfSalary(Long salary) {
        SaveJobOfferResource jobOfferRequest = new SaveJobOfferResource()
                .setSalary(salary)
                .setDescription("")
                .setType("")
                .setTitle("")
                .setDirection("");
        JobOfferResource response = restTemplate.postForObject("/api/employeers/" + employeerId + "/joboffers", jobOfferRequest, JobOfferResource.class);
        jobOfferId = response.getId();
    }

    @Then("I should be able to see my new jobOffer with salary of {long}")
    public void iShouldBeAbleToSeeMyNewJobOffer(Long salary) {
        JobOfferResource jobOfferResponse = restTemplate.getForObject(
                "/api/jobOffers/" + jobOfferId, JobOfferResource.class);
        assertThat(jobOfferResponse).isNotNull();
        assertThat(jobOfferResponse.getSalary()).isEqualTo(salary);
        assertThat(jobOfferResponse.getId()).isEqualTo(jobOfferId);
        cleanup();
    }

    public void cleanup() {
        restTemplate.delete("/api/employeers/" + employeerId);
        jobOfferRepository.deleteAll();
        assertThat(employeerRepository.findById(employeerId)).isEmpty();

    }
}
