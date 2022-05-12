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
    private String refreshToken;
    private String fullName;
    private String role;
    private Long restaurantId;
    private Long employeeId;
}
