package wisteria.cvapp.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthLoginUserDto {
    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "password is required")
    private String password;
}