package pl.restaurant.newsservice.unit;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.restaurant.newsservice.business.exception.IncorrectImageException;
import pl.restaurant.newsservice.business.exception.IncorrectImageExtensionException;
import pl.restaurant.newsservice.business.exception.InvalidImageSizeException;
import pl.restaurant.newsservice.business.utility.ImageValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageValidatorTest {

    @Test
    public void validateImageNullSuccess() {
        //given
        MultipartFile file = new MockMultipartFile("image.jpg", new byte[5]);
        //when
        //then
        assertDoesNotThrow(() -> ImageValidator.validateImageNull(file));
    }

    @Test
    public void validateImageNullFailure() {
        //given
        MultipartFile file = null;
        //when
        //then
        assertThrows(IncorrectImageException.class, () -> ImageValidator.validateImageNull(file));
    }

    @Test
    public void validateImageExtensionSuccess() {
        //given
        MultipartFile file = new MockMultipartFile("image.jpg", "image.jpg",
                "JPG", new byte[5]);
        //when
        //then
        assertDoesNotThrow(() -> ImageValidator.validateImageExtension(file));
    }

    @Test
    public void validateImageExtensionFailure() {
        //given
        MultipartFile file = new MockMultipartFile("image.mov", "image.mov",
                "JPG", new byte[5]);
        //when
        //then
        assertThrows(IncorrectImageExtensionException.class, () -> ImageValidator.validateImageExtension(file));
    }

    @Test
    public void validateImageSizeSuccess() {
        //given
        MultipartFile file = new MockMultipartFile("image.jpg", new byte[5]);
        //when
        //then
        assertDoesNotThrow(() -> ImageValidator.validateImageSize(file));
    }

    @Test
    public void validateImageSizeFailure() {
        //given
        MultipartFile file = new MockMultipartFile("image.jpg", new byte[5000001]);
        //when
        //then
        assertThrows(InvalidImageSizeException.class, () -> ImageValidator.validateImageSize(file));
    }
}
