package wisteria.cvapp.model.dto;

import lombok.Data;

@Data
public class JwtSignInUserDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private String role;
}
