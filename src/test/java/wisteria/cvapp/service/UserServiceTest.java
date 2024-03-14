package wisteria.cvapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import wisteria.cvapp.TestHelpers;
import wisteria.cvapp.model.dto.AuthLoginUserDto;
import wisteria.cvapp.model.dto.AuthRegisterUserDto;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest()
@Slf4j
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void getUserById() {
    }

    @Test
    void registerUser() {
        AuthRegisterUserDto authRegisterUserDto=new AuthRegisterUserDto();
        authRegisterUserDto.setEmail("nena.nena@gmail.com");
        authRegisterUserDto.setUsername("nenanena");
        authRegisterUserDto.setPassword("nena1998");
        authRegisterUserDto.setFirstName("Nena");
        authRegisterUserDto.setLastName("Nena");
        this.userService.registerUser(authRegisterUserDto);
    }

    @Test
    void editUser() {
    }

    @Test
    void changeUserPassword() {
    }

    @Test
    void loginUser() {
        AuthLoginUserDto authLoginUserDto = new AuthLoginUserDto();
        authLoginUserDto.setUsername("nenanena");
        String passwordRaw = "nena1998";

        authLoginUserDto.setPassword(passwordRaw);
        log.info(TestHelpers.objectToJson(userService.loginUser(authLoginUserDto)));
    }
}