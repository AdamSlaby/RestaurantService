package pl.restaurant.newsservice.business.utility;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;
import pl.restaurant.newsservice.business.exception.IncorrectImageException;
import pl.restaurant.newsservice.business.exception.IncorrectImageExtensionException;
import pl.restaurant.newsservice.business.exception.InvalidImageSizeException;

@UtilityClass
public class ImageValidator {
    private static final int MAX_SIZE = 5000000;

    public void validateImageNull(MultipartFile image) {
        if (image == null)
            throw new IncorrectImageException();
    }

    public void validateImageExtension(MultipartFile image) {
        int index = image.getOriginalFilename().replaceAll("\\\\", "/").lastIndexOf("/");
        String imageName = image.getOriginalFilename().substring(index + 1);
        String extension = imageName.split("\\.")[1];
        if (!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("jpeg"))
            throw new IncorrectImageExtensionException();
    }

    public void validateImageSize(MultipartFile image) {
        if (image.getSize() > MAX_SIZE)
            throw new InvalidImageSizeException();
    }
}
