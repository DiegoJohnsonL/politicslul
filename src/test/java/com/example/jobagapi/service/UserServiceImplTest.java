package com.example.jobagapi.service;

import com.example.jobagapi.domain.model.Studies;
import com.example.jobagapi.domain.model.User;
import com.example.jobagapi.domain.repository.UserRepository;
import com.example.jobagapi.domain.service.UserService;
import com.example.jobagapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @TestConfiguration
    static class UserServiceImplTestConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("when UpdateUser With Invalid User Then Throws ResourceNotFoundException") //happy path
    public void whenUpdateUserWithInvalidUserThenThrowsResourceNotFoundException() {
        //Arrange
        Long userId = 0L;
        String exceptedMessage = "Resource User not found for Id with value 0";
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        //Act
        Throwable throwable = catchThrowable(() -> userService.updateUser(userId, new User()));
        //Assert
        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);

    }

    @Test
    @DisplayName("when UpdateUser With Valid User Then Returns Success") //happy path
    public void whenUpdateUserWithValidUserThenReturnsSuccess() {
        //Arrange
        Long userId = 1L;
        User oldUser = new User().setFirstname("oldName");
        User request = new User().setFirstname("newName");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class))).then(i -> i.getArgument(0, User.class));
        //Act
        User result = userService.updateUser(userId, request);
        //Assert
        assertThat(result.getFirstname()).isEqualTo("newName");
    }

    @Test
    @DisplayName("when GetUserById With Valid Id Then Returns User") //happy path
    public void whenGetUserByIdWithValidIdThenReturnsUser() {
        //Arrange
        Long id = 1L;
        User user = new User().setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        //Act
        User foundUser = userService.getUserById(id);
        //Assert
        assertThat(foundUser.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("when GetUserById With Invalid Id Then Throws ResourceNotFoundException") //unhappy path
    public void whenGetUserByIdWithInvalidIdThenThrowsResourceNotFoundException() {
        //Arrange
        Long id = 1L;
        String template = "Resource %s not found for %s with value %s";
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        String exceptedMessage = String.format(template, "User", "Id", id);
        //Act
        Throwable exception = catchThrowable(() -> {
            User foundUser = userService.getUserById(id);
        });
        //Assert
        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(exceptedMessage);
    }
}


