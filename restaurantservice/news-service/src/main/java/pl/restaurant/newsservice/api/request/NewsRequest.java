package pl.restaurant.newsservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequest {
    @NotNull(message = "Identyfikator pracownika nie może być pusty")
    private Long employeeId;

    private MultipartFile image;

    @NotBlank(message = "Tytuł wiadomości nie może być psuty")
    @Size(max = 200, message = "Tytuł jest zbyt długi")
    private String title;

    @NotBlank(message = "Zawartość wiadomości nie może być pusta")
    @Size(max = 2500, message = "Zawartość wiadomości jest zbyt długa")
    private String content;
}
