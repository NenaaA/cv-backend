package wisteria.cvapp.model.dto;

import lombok.Data;

@Data
public class JwtSignInUserDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private String email;
    private String fullName;
    private String role;
}
