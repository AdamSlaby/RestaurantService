package pl.restaurant.employeeservice.unit;

import org.junit.jupiter.api.Test;
import pl.restaurant.employeeservice.business.utility.CredentialsGenerator;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CredentialsGeneratorTest {

    @Test
    public void generatePasswordSuccess() {
        //given
        Pattern lowercase = Pattern.compile("^.*[a-ząćęłńóśźż].*[a-ząćęłńóśźż].*[a-ząćęłńóśźż].*[a-ząćęłńóśźż].*[a-ząćęłńóśźż].*$");
        Pattern uppercase = Pattern.compile("^.*[A-ZĄĆĘŁŃÓŚŹŻ].*$");
        Pattern digits = Pattern.compile("^.*\\d.*\\d.*\\d.*$");
        Pattern specialCharacters = Pattern.compile("^.*[!@#$%^&*()_+].*$");
        //when
        String password = CredentialsGenerator.generatePassword();
        //then
        assertNotNull(password);
        assertThat(password.length()).isEqualTo(10);
        assertThat(lowercase.matcher(password).find()).isEqualTo(true);
        assertThat(uppercase.matcher(password).find()).isEqualTo(true);
        assertThat(digits.matcher(password).find()).isEqualTo(true);
        assertThat(specialCharacters.matcher(password).find()).isEqualTo(true);
    }

    @Test
    public void generateUsernameSuccess() {
        //given
        String name = "Adam";
        Long id = 1L;
        //when
        String username = CredentialsGenerator.generateUsername(name, id);
        //then
        assertNotNull(username);
        assertThat(username).isEqualTo("adam-1");
    }
}
