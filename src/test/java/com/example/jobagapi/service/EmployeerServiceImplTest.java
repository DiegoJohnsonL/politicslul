package com.example.jobagapi.service;

import com.example.jobagapi.domain.model.Employeer;
import com.example.jobagapi.domain.model.Postulant;
import com.example.jobagapi.domain.repository.EmployeerRepository;
import com.example.jobagapi.domain.repository.UserRepository;
import com.example.jobagapi.domain.service.EmployeerService;
import com.example.jobagapi.exception.ResourceIncorrectData;
import com.example.jobagapi.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class EmployeerServiceImplTest {

    @MockBean
    private EmployeerRepository employeerRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private EmployeerService employeerService;

    @TestConfiguration
    static class EmployeerServiceImplTestConfiguration {
        @Bean
        public EmployeerService employeerService() {
            return new EmployeerServiceImpl();
        }
    }

    @Test
    @DisplayName("When getEmployeerById That Exists Then Returns Employeer")
    public void whenGetEmployeerByIdThatExistsThenReturnsEmployeer() {
        // Arrange
        Long Id = 1L;
        Employeer employeer = (Employeer) new Employeer().setId(Id);
        when(employeerRepository.findById(Id)).thenReturn(Optional.of(employeer));
        // Act
        Employeer foundEmployeer = employeerService.getEmployeerById(Id);
        // Assert
        assertThat(foundEmployeer.getId()).isEqualTo(Id);
    }

    @Test
    @DisplayName("When getEmployeerById That does not exist Then Throws ResourceNotFoundException")
    public void whenGetEmployeerByIdThatDoesNotExistThenThrowsResourceNotFoundException() {
        //Arrange
        Employeer employeer = new Employeer();
        String exceptedMessage = "Resource Employeer not found for Id with value 1";
        when(employeerRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Act
        Throwable throwable = catchThrowable(() -> employeerService.getEmployeerById(1L));
        //Assert
        Assertions.assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);
    }


}