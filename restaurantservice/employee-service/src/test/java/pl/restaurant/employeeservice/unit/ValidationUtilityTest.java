package pl.restaurant.employeeservice.unit;

import org.junit.jupiter.api.Test;
import pl.restaurant.employeeservice.business.exception.InvalidAccountException;
import pl.restaurant.employeeservice.business.exception.InvalidPeselException;
import pl.restaurant.employeeservice.business.utility.ValidationUtility;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUtilityTest {

    @Test
    public void validatePeselSuccess() {
        //given
        String pesel = "06222157267";
        //when
        //then
        assertDoesNotThrow(() -> ValidationUtility.validatePesel(pesel));
    }

    @Test
    public void validatePeselFailure() {
        //given
        String pesel = "06222157266";
        //when
        //then
        assertThrows(InvalidPeselException.class, () -> ValidationUtility.validatePesel(pesel));
    }

    @Test
    public void validateAccountNrSuccess() {
        //given
        String accountNr = "72109024025463152737292491";
        //when
        //then
        assertDoesNotThrow(() -> ValidationUtility.validateAccountNr(accountNr));
    }

    @Test
    public void validateAccountNrFailure() {
        //given
        String accountNr = "72109024025463152737292492";
        //when
        //then
        assertThrows(InvalidAccountException.class, () -> ValidationUtility.validateAccountNr(accountNr));
    }
}
