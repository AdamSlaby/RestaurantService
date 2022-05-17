package pl.restaurant.menuservice.business.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;
import pl.restaurant.menuservice.business.exception.CannotSaveImageException;

import java.io.*;

@UtilityClass
@Log4j2
public class FileUtility {

    public String fileUpload(MultipartFile image, String path) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileName = getUniqueFilename(image);
        File imageFile = new File(path + "/" + fileName);
        try {
            inputStream = image.getInputStream();
            if (!imageFile.exists())
                imageFile.createNewFile();
            outputStream = new FileOutputStream(imageFile);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1)
                outputStream.write(bytes, 0, read);
            outputStream.close();
            return fileName;
        } catch (IOException e) {
            throw new CannotSaveImageException();
        }
    }

    public boolean deleteFile(String filename, String path) {
        File fileToDelete = new File(path + "/" + filename);
        return fileToDelete.delete();
    }

    public String getUniqueFilename(MultipartFile image) {
        StringBuilder builder = new StringBuilder();
        int index = image.getOriginalFilename().replaceAll("\\\\", "/").lastIndexOf("/");
        String imageName = image.getOriginalFilename().substring(index + 1);
        String[] imageNameParts = imageName.split("\\.");
        String timestamp = String.valueOf(System.currentTimeMillis());
        return builder.append(imageNameParts[0])
                .append("_")
                .append(timestamp)
                .append(".")
                .append(imageNameParts[1]).toString();
    }
}
