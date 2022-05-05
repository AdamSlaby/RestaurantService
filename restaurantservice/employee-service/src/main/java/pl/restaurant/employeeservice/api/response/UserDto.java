package pl.restaurant.employeeservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}
