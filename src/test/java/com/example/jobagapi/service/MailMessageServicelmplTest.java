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

import com.example.jobagapi.domain.model.*;
import com.example.jobagapi.domain.repository.EmployeerRepository;
import com.example.jobagapi.domain.repository.MailMessageRepository;
import com.example.jobagapi.domain.repository.PostulantRepository;
import com.example.jobagapi.domain.service.MailMessageService;
import com.example.jobagapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class MailMessageServicelmplTest{

    @TestConfiguration
    static class MailMessageTestConfiguration {
        @Bean
        public MailMessageService MailMessageService() {
            return new MailMessageServiceImpl();
        }
    }

    @MockBean
    private PostulantRepository postulantRepository;
    @MockBean
    private MailMessageRepository mailMessageRepository;
    @MockBean
    private EmployeerRepository employeerRepository;
    @MockBean
    private MailMessageService MailMessageService;

    @Test
    public void createMailMessageTest(){
        Long postulantId=1L;
        Long employeerId=1L;
        Postulant existingPostulant = new Postulant();
        Employeer existingEmployeer = new Employeer();
        given(!postulantRepository.existsById(postulantId)).willReturn(false);
        given(!employeerRepository.existsById(employeerId)).willReturn(false);

        given(postulantRepository.findById(postulantId)).willReturn(Optional.of(existingPostulant));
        given(employeerRepository.findById(employeerId)).willReturn(Optional.of(existingEmployeer));

        given(mailMessageRepository.save(any(MailMessage.class))).willAnswer(i -> i.getArgument(0, MailMessage.class));

        MailMessage MailMessageRequest = new MailMessage();
        MailMessage actual = MailMessageService.createMailMessage(postulantId, employeerId, MailMessageRequest);

    }

    @Test //unhappy path
    public void createMailMessageWhenThePostulantExistTest(){
        Long postulantId=1L;
        Long employeerId=1L;       
        MailMessage MailMessageRequest = new MailMessage();
        String  ExceptedMessage=" Registered Employeer";
        given(!postulantRepository.existsById(postulantId)).willReturn(true);
        given(!employeerRepository.existsById(employeerId)).willReturn(true);

        Throwable throwable = catchThrowable(() -> MailMessageService.createMailMessage(postulantId, employeerId, MailMessageRequest));
        
        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(ExceptedMessage);

    }

}