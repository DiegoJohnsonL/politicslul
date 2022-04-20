package com.example.jobagapi.service;

import com.example.jobagapi.domain.model.Studies;
import com.example.jobagapi.domain.repository.StudiesRepository;
import com.example.jobagapi.domain.service.StudiesService;
import com.example.jobagapi.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class StudiesServiceImplTest {

    @MockBean
    private StudiesRepository studiesRepository;

    @Autowired
    private StudiesService studiesService;

    @TestConfiguration
    static class StudiesServiceImplTestConfiguration {
        @Bean
        public StudiesService studiesService() {
            return new StudiesServiceImpl();
        }
    }

    @Test
    public void getStudiesByIdWhenFoundTest() {
        //Arrange
        Long studiesId = 1L;
        Studies studies = new Studies().setId(1L).setName("name").setDegree(1L);
        when(studiesRepository.findById(1L)).thenReturn(Optional.of(studies));
        //Act
        Studies result = studiesService.getStudiesById(studiesId);
        //Assert
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDegree()).isEqualTo(1L);
    }

    @Test
    public void getStudiesByIdWhenNotFoundTest() {
        //Arrange
        Long studiesId = 0L;
        String exceptedMessage = "Resource Studies not found for Id with value 0";
        when(studiesRepository.findById(0L)).thenReturn(Optional.empty());
        //Act
        Throwable throwable = catchThrowable(() -> studiesService.getStudiesById(studiesId));
        //Assert
        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);
    }

    @Test
    public void updateStudiesByIdWhenFoundTest() {
        //Arrange
        Long studiesId = 1L;
        Studies oldStudies = new Studies().setName("oldName").setDegree(0L);
        Studies request = new Studies().setName("newName").setDegree(1L);
        when(studiesRepository.findById(1L)).thenReturn(Optional.of(oldStudies));
        when(studiesRepository.save(any(Studies.class))).then(i -> i.getArgument(0, Studies.class));
        //Act
        Studies result = studiesService.updateStudies(studiesId, request);
        //Assert
        assertThat(result.getName()).isEqualTo("newName");
        assertThat(result.getDegree()).isEqualTo(1L);
    }

    @Test
    public void updateStudiesByIdWhenNotFoundTest() {
        //Arrange
        Long studiesId = 0L;
        String exceptedMessage = "Resource Studies not found for Id with value 0";
        when(studiesRepository.findById(0L)).thenReturn(Optional.empty());
        //Act
        Throwable throwable = catchThrowable(() -> studiesService.updateStudies(studiesId, new Studies()));
        //Assert
        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);
    }

}