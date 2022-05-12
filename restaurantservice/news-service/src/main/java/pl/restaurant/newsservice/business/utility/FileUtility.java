package pl.restaurant.newsservice.business.utility;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@UtilityClass
public class FileUtility {

    public String fileUpload(MultipartFile image) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileName = image.getOriginalFilename();
        File newFile = new File(fileName);

        try {
            inputStream = image.getInputStream();

            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
