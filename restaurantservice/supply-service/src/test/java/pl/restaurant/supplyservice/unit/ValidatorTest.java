package pl.restaurant.supplyservice.unit;

import org.junit.jupiter.api.Test;
import pl.restaurant.supplyservice.business.exception.InvalidNipException;
import pl.restaurant.supplyservice.business.utility.Validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    public void validateNipSuccess() {
        //given
        String nip = "5357883081";
        String fieldName = "buyerNip";
        //when
        //then
        assertDoesNotThrow(() -> Validator.validateNip(nip, fieldName));
    }

    @Test
    public void validateNipFailure() {
        //given
        String nip = "5357883080";
        String fieldName = "buyerNip";
        //when
        //then
        assertThrows(InvalidNipException.class, () -> Validator.validateNip(nip, fieldName));
    }
}
