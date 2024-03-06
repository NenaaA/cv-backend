package wisteria.cvapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wisteria.cvapp.model.User;
import wisteria.cvapp.model.dto.AuthLoginUserDto;
import wisteria.cvapp.model.dto.AuthRegisterUserDto;
import wisteria.cvapp.model.dto.JwtSignInUserDto;
import wisteria.cvapp.repository.UserRepository;

import wisteria.cvapp.service.UserService;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;



    @Override
    public User getUserById(Integer userId) {
        return this.userRepository.getUserById(userId);
    }

    @Override
    public void registerUser(AuthRegisterUserDto authRegisterUserDto) {
        User user = new User();
        user.setEmail(authRegisterUserDto.getEmail());
        user.setPassword(authRegisterUserDto.getPassword());
        user.setName(authRegisterUserDto.getFirstName());
        user.setSurname(authRegisterUserDto.getLastName());
        this.userRepository.save(user);
    }

    @Override
    public void editUser(AuthRegisterUserDto authRegisterUserDto) {
        User user = this.userRepository.findByEmail(authRegisterUserDto.getEmail());
        if (user == null) {
            log.error("Error while editing the user with email={}", authRegisterUserDto.getEmail());
            throw new RuntimeException("Invalid email");
        }
        if (!authRegisterUserDto.getPassword().isEmpty())
            user.setPassword(authRegisterUserDto.getPassword());
        if (authRegisterUserDto.getFirstName() != null)
            user.setName(authRegisterUserDto.getFirstName());
        if (authRegisterUserDto.getLastName() != null)
            user.setSurname(authRegisterUserDto.getLastName());
        this.userRepository.save(user);
    }

    @Override
    public void changeUserPassword(AuthLoginUserDto authLoginUserDto) {
        User user = this.userRepository.findByEmail(authLoginUserDto.getEmail());
        if (user == null) {
            log.error("Error while changing password for the user with email={}", authLoginUserDto.getEmail());
            throw new RuntimeException("Invalid email");
        }
        user.setPassword(authLoginUserDto.getPassword());
        this.userRepository.save(user);
    }

    @Override
    public JwtSignInUserDto loginUser(AuthLoginUserDto authLoginUserDto) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(authLoginUserDto.getEmail(), authLoginUserDto.getPassword()));

        User user = this.userRepository.findByEmail(authLoginUserDto.getEmail());

        //SecurityContextHolder.getContext().setAuthentication(authentication);
        //String jwt = jwtUtils.generateJwtToken(authentication);

        //UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        JwtSignInUserDto jwtSignInUserDto = new JwtSignInUserDto();
        //jwtSignInUserDto.setAccessToken(jwt);
        //jwtSignInUserDto.setEmail(userDetails.getUsername());
        jwtSignInUserDto.setFullName(user.getName() + " " + user.getSurname());
        jwtSignInUserDto.setRole(user.getRole());

        return jwtSignInUserDto;
    }
}
