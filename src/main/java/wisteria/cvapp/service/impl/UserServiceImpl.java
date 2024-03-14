package wisteria.cvapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wisteria.cvapp.model.User;
import wisteria.cvapp.model.dto.AuthLoginUserDto;
import wisteria.cvapp.model.dto.AuthRegisterUserDto;
import wisteria.cvapp.model.dto.JwtSignInUserDto;
import wisteria.cvapp.repository.UserRepository;

import wisteria.cvapp.security.jwt.JwtUtils;
import wisteria.cvapp.security.service.UserDetailsImpl;
import wisteria.cvapp.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;



    @Override
    public User getUserById(Integer userId) {
        return this.userRepository.getUserById(userId);
    }

    @Override
    public void registerUser(AuthRegisterUserDto authRegisterUserDto) {
        User userExists=userRepository.findByUsername(authRegisterUserDto.getUsername());
        if(userExists!=null)
        {
            log.error("Error while creating a new user for the user with username={}", authRegisterUserDto.getUsername());
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(authRegisterUserDto.getUsername());
        user.setEmail(authRegisterUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(authRegisterUserDto.getPassword()));
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
        User user = this.userRepository.findByUsername(authLoginUserDto.getUsername());
        if (user == null) {
            log.error("Error while changing password for the user with username={}", authLoginUserDto.getUsername());
            throw new RuntimeException("Invalid username");
        }
        user.setPassword(authLoginUserDto.getPassword());
        this.userRepository.save(user);
    }

    @Override
    public JwtSignInUserDto loginUser(AuthLoginUserDto authLoginUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authLoginUserDto.getUsername(), authLoginUserDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtSignInUserDto jwtSignInUserDto = new JwtSignInUserDto();
        jwtSignInUserDto.setAccessToken(jwt);
        jwtSignInUserDto.setEmail(userDetails.getEmail());
        jwtSignInUserDto.setUsername(userDetails.getUsername());
        jwtSignInUserDto.setRole(roles.get(0));

        return jwtSignInUserDto;
    }
}
