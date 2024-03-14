package wisteria.cvapp.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRegisterUserDto {
    @NotNull(message = "username is required")
    private String username;
    @NotNull(message = "email is required")
    private String email;
    private String firstName;
    private String lastName;
    @NotNull(message = "password is required")
    private String password;
}
