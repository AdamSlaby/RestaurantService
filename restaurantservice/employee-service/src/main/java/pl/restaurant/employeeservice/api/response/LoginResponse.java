package pl.restaurant.employeeservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String fullName;
    private String role;
}
