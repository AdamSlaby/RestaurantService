package pl.restaurant.menuservice.unit;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import pl.restaurant.menuservice.business.utility.FileUtility;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FileUtilityTest {

    @Test
    public void fileUploadSuccess() {
        //given
        String name = "test.jpg";
        String path = new FileSystemResource("").getFile().getAbsolutePath() + "/";
        MockMultipartFile file = new MockMultipartFile(name, name,
                "JPG", new byte[4]);
        //when
        String filename = FileUtility.fileUpload(file, path);
        new FileSystemResource(filename).getFile().deleteOnExit();
        //then
        assertNotNull(filename);
        assertTrue(new File(path + filename).exists());
        assertTrue(filename.contains(name.split("\\.")[0]));
    }

    @Test
    public void deleteFileSuccess() {
        //given
        String name = "test.jpg";
        String path = new FileSystemResource("").getFile().getAbsolutePath() + "/";
        MockMultipartFile file = new MockMultipartFile(name, name,
                "JPG", new byte[4]);
        String filename = FileUtility.fileUpload(file, path);
        //when
        boolean isDeleted = FileUtility.deleteFile(filename, path);
        File deletedFile = new File(path +  filename);
        new FileSystemResource(filename).getFile().deleteOnExit();
        //then
        assertTrue(isDeleted);
        assertFalse(deletedFile.exists());
    }

    @Test
    public void deleteFileFailure() {
        //given
        String filename = "test.jpg";
        String path = new FileSystemResource("").getFile().getAbsolutePath() + "/";
        //when
        boolean isDeleted = FileUtility.deleteFile(filename, path);
        File deletedFile = new File(path + filename);
        //then
        assertFalse(isDeleted);
        assertFalse(deletedFile.exists());
    }

    @Test
    public void getUniqueFilenameSuccess() {
        //given
        String name = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(name, name,
                "JPG", new byte[4]);
        //when
        String filename = FileUtility.getUniqueFilename(file);
        //then
        assertThat(filename).isNotEqualTo(name);
        assertTrue(filename.contains(name.split("\\.")[0]));
    }
}
