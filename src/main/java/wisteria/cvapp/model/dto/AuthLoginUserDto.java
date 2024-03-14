package wisteria.cvapp.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthLoginUserDto {
    @NotNull(message = "username is required")
    private String username;
    @NotNull(message = "password is required")
    private String password;
}
