package com.example.jobagapi.service;

import com.example.jobagapi.domain.model.Postulant;
import com.example.jobagapi.domain.model.User;
import com.example.jobagapi.domain.repository.PostulantRepository;
import com.example.jobagapi.domain.repository.UserRepository;
import com.example.jobagapi.domain.service.PostulantService;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PostulantServiceImplTest {

    @MockBean
    private PostulantRepository postulantRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private PostulantService postulantService;

    @TestConfiguration
    static class PostulantServiceImplTestConfiguration {
        @Bean
        public PostulantService postulantService() {
            return new PostulantServiceImpl();
        }
    }

    @Test
    @DisplayName("when Create Postulant With Valid Email Then Returns Success") //happy path
    public void whenCreatePostulantWithValidEmailThenReturnsSuccess() {
        //Arrange
        Postulant postulant = (Postulant) new Postulant().setFirstname("name").setEmail("test@gmail.com");
        when(postulantRepository.save(any(Postulant.class))).then(i -> i.getArgument(0, Postulant.class));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        //Act
        Postulant savedPostulant = postulantService.createPostulant(postulant);
        //Assert
        assertThat(savedPostulant.getEmail()).isEqualTo("test@gmail.com");
        assertNotNull(savedPostulant);
    }

    @Test
    @DisplayName("when Create Postulant With Used Email Then Throws ResourceIncorrectData") //happy path
    public void whenCreatePostulantWithUsedEmailThenThrowsResourceIncorrectData() {
        //Arrange
        Postulant postulant = (Postulant) new Postulant().setFirstname("name").setEmail("test@gmail.com");
        String exceptedMessage = "El email ya esta en uso";
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //Act
        Throwable throwable = catchThrowable(() -> postulantService.createPostulant(postulant));
        //Assert
        Assertions.assertThat(throwable)
                .isInstanceOf(ResourceIncorrectData.class)
                .hasMessage(exceptedMessage);
    }

    @Test
    @DisplayName("when GetPostulantById With Valid Id Then Returns Postulant") //happy path
    public void whenGetPostulantByIdWithValidIdThenReturnsPostulant() {
        //Arrange
        Long id = 1L;
        Postulant postulant = new Postulant(id, "caro", "Villegas", "email", 2L, "password", "document", "civil");
        when(postulantRepository.findById(id)).thenReturn(Optional.of(postulant));
        //Act
        Postulant foundPostulant = postulantService.getPostulantById(id);
        //Assert
        assertThat(foundPostulant.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("when GetPostulantById With Invalid Id Then Throws ResourceNotFoundException") //unhappy path
    public void whenGetPostulantByIdWithInvalidIdThenThrowsResourceNotFoundException() {
        //Arrange
        Long id = 1L;
        String exceptedMessage = "Resource Postulant not found for Id with value 1";
        when(postulantRepository.findById(id)).thenReturn(Optional.empty());
        //Act
        Throwable exception = catchThrowable(() -> postulantService.getPostulantById(id));
        //Assert
        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);
    }

    @Test
    @DisplayName("when UpdatePostulant With Valid Postulant Then Returns Success") //happy path
    public void whenUpdatePostulantWithValidPostulantThenReturnsSuccess() {
        //Arrange
        Long postulantId = 1L;
        Postulant oldPostulant = new Postulant().setCivil_status("oldStatus");
        Postulant request = new Postulant().setCivil_status("newStatus");
        when(postulantRepository.findById(postulantId)).thenReturn(Optional.of(oldPostulant));
        when(postulantRepository.save(any(Postulant.class))).then(i -> i.getArgument(0, Postulant.class));
        //Act
        Postulant result = postulantService.updatePostulant(postulantId, request);
        //Assert
        Assertions.assertThat(result.getCivil_status()).isEqualTo("newStatus");
    }
}
