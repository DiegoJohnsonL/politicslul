package com.example.jobagapi.cucumber.glue;

import com.example.jobagapi.domain.model.Employeer;
import com.example.jobagapi.domain.model.JobOffer;
import com.example.jobagapi.domain.model.Postulant;
import com.example.jobagapi.domain.repository.EmployeerRepository;
import com.example.jobagapi.domain.repository.JobOfferRepository;
import com.example.jobagapi.domain.repository.PostulantJobRepository;
import com.example.jobagapi.domain.repository.PostulantRepository;
import com.example.jobagapi.exception.ExceptionResponse;
import com.example.jobagapi.resource.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ApplyJobSteps {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    PostulantRepository postulantRepository;
    @Autowired
    private EmployeerRepository employeerRepository;
    @Autowired
    private JobOfferRepository jobOfferRepository;
    @Autowired
    private PostulantJobRepository postulantJobRepository;

    private Long jobOfferId;
    private Long postulantId;
    private Long employeerId;
    private Long jobApplicationId;
    private String expectedMessage;

    @Given("I am a postulant and look for a new job opportunity")
    public void iAmAPostulantAndLookForANewJobOpportunity() {
        SavePostulantResource postulantRequest = (SavePostulantResource) new SavePostulantResource()
                .setPassword("")
                .setNumber(0L)
                .setFirstname("")
                .setLastname("")
                .setEmail("postulant@email.com");
        PostulantResource postulantResponse = restTemplate
                .postForObject("/api/postulants", postulantRequest, PostulantResource.class);
        postulantId = postulantResponse.getId();
    }

    @And("The job offer have a stable minimum {long}")
    public void theJobOfferHaveAStableMinimumSalaryAndId(Long salary) {
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
        SaveJobOfferResource jobOfferRequest = new SaveJobOfferResource()
                .setSalary(salary)
                .setDescription("")
                .setType("")
                .setTitle("")
                .setDirection("");
        JobOfferResource jobOfferResponse = restTemplate
                .postForObject("/api/employeers/" + employeerId + "/joboffers", jobOfferRequest, JobOfferResource.class);
        jobOfferId = jobOfferResponse.getId();
    }

    @When("I apply for the job")
    public void iApplyForTheJob() {
        PostulantJobResource jobApplicationResponse = applyForJobOffer();
        jobApplicationId = jobApplicationResponse.getId();
        assertThat(jobApplicationResponse.isAceppt()).isFalse();
    }

    @Then("I should be able to see my new application")
    public void iShouldBeAbleToSeeMyNewApplication() {
        PostulantJobResource jobApplicationResponse = restTemplate.getForObject(
                "/api/postulantjobs/" + jobApplicationId, PostulantJobResource.class);
        assertThat(jobApplicationResponse.getIdPostulant()).isEqualTo(postulantId);
        assertThat(jobApplicationResponse.getIdJobOffer()).isEqualTo(jobOfferId);
        cleanup();
    }

    @Given("I am a postulant look for job opportunities")
    public void iAmAPostulantLookForJobOpportunities() {
        Postulant postulant = (Postulant) new Postulant().setPassword("")
                .setNumber(0L)
                .setFirstname("")
                .setLastname("")
                .setEmail("postulant@email.com");
        postulantRepository.save(postulant);
        postulantId = postulant.getId();
    }

    @And("I have applied to a jobOffer")
    public void iHaveAppliedToAJobOffer() {
        Employeer employeer = (Employeer) new Employeer()
                .setPassword("")
                .setNumber(1L)
                .setFirstname("")
                .setLastname("")
                .setEmail("eployeer@email.com");
        employeerRepository.save(employeer);
        employeerId = employeer.getId();
        JobOffer jobOffer = new JobOffer()
                .setEmployeer(employeer)
                .setSalary(950L)
                .setDescription("")
                .setType("")
                .setTitle("")
                .setDirection("");
        jobOfferRepository.save(jobOffer);
        jobOfferId = jobOffer.getId();
        PostulantJobResource jobApplicationResponse = applyForJobOffer();
        jobApplicationId = jobApplicationResponse.getId();
        assertThat(jobApplicationResponse.isAceppt()).isFalse();
    }

    @When("I try to apply to the same job Offer")
    public void iTryToApplyToTheSameJobOffer() {
        SavePostulantJobResource jobApplicationRequest = new SavePostulantJobResource(false);
        ExceptionResponse response = restTemplate.postForObject(
                "/api/postulants/" + postulantId + "/joboffers/" + jobOfferId + "/postulantjobs",
                jobApplicationRequest, ExceptionResponse.class);
        expectedMessage = response.getMessage();
    }

    @Then("I should get the error with message {string}")
    public void iShouldGetTheErrorWithMessageError(String error) {
        Assertions.assertThat(expectedMessage).isEqualTo(error);
        cleanup();
    }

    public PostulantJobResource applyForJobOffer() {
        SavePostulantJobResource jobApplicationRequest = new SavePostulantJobResource(false);
        return restTemplate.postForObject(
                "/api/postulants/" + postulantId + "/joboffers/" + jobOfferId + "/postulantjobs",
                jobApplicationRequest,
                PostulantJobResource.class);
    }

    public void cleanup() {
        postulantRepository.deleteAll();
        employeerRepository.deleteAll();
        jobOfferRepository.deleteAll();
        postulantJobRepository.deleteAll();
    }
}
