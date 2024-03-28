package wisteria.cvapp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wisteria.cvapp.model.dto.AuthLoginUserDto;
import wisteria.cvapp.model.dto.AuthRegisterUserDto;
import wisteria.cvapp.model.dto.JwtSignInUserDto;
import wisteria.cvapp.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public HttpStatus registerUser(@RequestBody AuthRegisterUserDto authRegisterUserDto){
        this.userService.registerUser(authRegisterUserDto);
        return HttpStatus.OK;
    }

    @PostMapping("/signIn")
    public ResponseEntity<JwtSignInUserDto> loginUser(@RequestBody AuthLoginUserDto authLoginUserDto){
        return ResponseEntity.ok()
                .body(this.userService.loginUser(authLoginUserDto));
    }

    @PatchMapping("/user/changePassword")
    public HttpStatus changePasswordForUser(@RequestBody AuthLoginUserDto authLoginUserDto){
        this.userService.changeUserPassword(authLoginUserDto);
        return HttpStatus.OK;
    }

    @PatchMapping("/user/editProfile")
    public HttpStatus editProfile(@RequestBody AuthRegisterUserDto authRegisterUserDto){
        this.userService.editUser(authRegisterUserDto);
        return HttpStatus.OK;
    }
}
