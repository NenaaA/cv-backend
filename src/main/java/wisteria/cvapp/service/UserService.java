package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import wisteria.cvapp.model.User;
import wisteria.cvapp.model.dto.AuthLoginUserDto;
import wisteria.cvapp.model.dto.AuthRegisterUserDto;
import wisteria.cvapp.model.dto.JwtSignInUserDto;

public interface UserService {
    User getUserById(@NotNull Integer userId);

    void registerUser(@NotNull AuthRegisterUserDto authRegisterUserDto);

    void editUser(@NotNull AuthRegisterUserDto authRegisterUserDto);

    void changeUserPassword(@NotNull AuthLoginUserDto authLoginUserDto);

    JwtSignInUserDto loginUser(@NotNull AuthLoginUserDto authLoginUserDto);

    boolean checkJwtAuthentication(@NonNull Integer userId);

    User getLoggedInUser();

}
