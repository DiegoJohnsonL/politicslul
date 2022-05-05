package com.example.jobagapi.service;

import com.example.jobagapi.domain.model.Interview;
import com.example.jobagapi.domain.model.JobOffer;
import com.example.jobagapi.domain.model.Postulant;
import com.example.jobagapi.domain.model.Sector;
import com.example.jobagapi.domain.repository.InterviewRepository;
import com.example.jobagapi.domain.repository.JobOfferRepository;
import com.example.jobagapi.domain.repository.PostulantRepository;
import com.example.jobagapi.domain.model.Company;
import com.example.jobagapi.domain.model.Employeer;
import com.example.jobagapi.domain.repository.CompanyRepository;
import com.example.jobagapi.domain.repository.EmployeerRepository;
import com.example.jobagapi.domain.repository.SectorRepository;
import com.example.jobagapi.domain.service.CompanyService;


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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)

public class CompanyServiceImplTest {
    
    @TestConfiguration
    static class CompanyServiceImplTestConfiguration {
        @Bean
        public CompanyService companyService() {
            return new CompanyServiceImpl();
        }
    }

    @MockBean
    private EmployeerRepository employeerRepository;
    @MockBean
    private SectorRepository sectorRepository;
    @MockBean
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyService companyService;

    @Test
    public void createCompanyTest(){
        Long employeerId=1L;
        Long sectorId=1L;
        Employeer existingEmployeer = new Employeer();
        Sector existingSector = new Sector();


        given(companyRepository.existsByEmployeerId(employeerId)).willReturn(false);
        
        given(!employeerRepository.existsById(employeerId)).willReturn(false);
        given(!sectorRepository.existsById(sectorId)).willReturn(false);

        given(employeerRepository.findById(employeerId)).willReturn(Optional.of(existingEmployeer));
        given(sectorRepository.findById(sectorId)).willReturn(Optional.of(existingSector));
        given(companyRepository.save(any(Company.class))).willAnswer(i -> i.getArgument(0, Company.class));

        Company companyrequest = new Company();
        Company actual = companyService.createCompany(employeerId, sectorId ,companyrequest);
        //Then
        assertThat(actual.getSector()).isEqualTo(existingSector);
    }

    @Test //unhappy Path
    public void crateCompanyAlreadyHavaCreate(){
        Long employeerId = 1L;
        Long sectorId = 1L;
        Company companyrequest = new Company();
        String exceptedMessage = "La compania ya fue registrado por el empleador";

        given(companyRepository.existsByEmployeerId(employeerId)).willReturn(true);

        Throwable throwable = catchThrowable(() -> companyService.createCompany(employeerId, sectorId, companyrequest));
        //Then
        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);      
    }

}
