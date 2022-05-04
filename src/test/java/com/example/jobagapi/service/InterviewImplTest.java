package com.example.jobagapi.service;

import com.example.jobagapi.domain.model.Interview;
import com.example.jobagapi.domain.model.JobOffer;
import com.example.jobagapi.domain.model.Postulant;
import com.example.jobagapi.domain.repository.InterviewRepository;
import com.example.jobagapi.domain.repository.JobOfferRepository;
import com.example.jobagapi.domain.repository.PostulantRepository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jobagapi.domain.service.InterviewService;
import com.example.jobagapi.domain.service.PostulantService;
import com.example.jobagapi.domain.service.StudiesService;
import com.example.jobagapi.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class InterviewImplTest {

    @TestConfiguration
    static class InterviewImplTestConfiguration {
        @Bean
        public InterviewService interviewService() {
            return new InterviewImpl();
        }
    }

    @MockBean
    private PostulantRepository postulantRepository;
    @MockBean
    private JobOfferRepository jobOfferRepository;
    @MockBean
    private InterviewRepository interviewRepository;
    @Autowired
    private InterviewService interviewService;

    @Test
    public void createInterviewTest() {
        //given
        Long postulantId = 1L;
        Long jobOfferId = 1L;
        Postulant existingPostulant = new Postulant();
        JobOffer existingJobOffer = new JobOffer();
        given(interviewRepository.existsByPostulantId(postulantId)).willReturn(false);
        given(interviewRepository.existsByJobOfferId(jobOfferId)).willReturn(false);
        given(postulantRepository.findById(postulantId)).willReturn(Optional.of(existingPostulant));
        given(jobOfferRepository.findById(jobOfferId)).willReturn(Optional.of(existingJobOffer));
        given(interviewRepository.save(any(Interview.class))).willAnswer(i -> i.getArgument(0, Interview.class));
        //When
        Interview interviewRequest = new Interview();
        Interview actual = interviewService.createInterview(postulantId, jobOfferId, interviewRequest);
        //Then
        assertThat(actual.getPostulant()).isEqualTo(existingPostulant);
    }

    @Test //unhappy path
    public void createInterviewWhenAlreadyHaveAnInterviewTest() {
        Long postulantId = 1L;
        Long jobOfferId = 1L;
        Interview interviewRequest = new Interview();
        String exceptedMessage = "El postulante ya tiene programado una entrevista con la oferta de trabajo";
        given(interviewRepository.existsByPostulantId(postulantId)).willReturn(true);
        given(interviewRepository.existsByJobOfferId(jobOfferId)).willReturn(true);

        Throwable throwable = catchThrowable(() -> interviewService.createInterview(postulantId, jobOfferId, interviewRequest));

        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);
    }
}