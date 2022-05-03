package pl.restaurant.employeeservice.business.utility;

import lombok.experimental.UtilityClass;
import pl.restaurant.employeeservice.business.exception.InvalidAccountException;
import pl.restaurant.employeeservice.business.exception.InvalidPeselException;

import java.math.BigInteger;

@UtilityClass
public class ValidationUtility {
    public void validatePesel(String pesel) {
        int[] wages = new int[]{1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;
        for (int i = 0; i < wages.length; i++)
            sum += Integer.parseInt(pesel.charAt(i) + "") * wages[i];
        sum %= 10;
        if (Integer.parseInt(pesel.charAt(10) + "") != (10 - sum))
            throw new InvalidPeselException();
    }

    public void validateAccountNr(String accountNr) {
        StringBuilder iban = new StringBuilder(accountNr.replace(" ", "").substring(2));
        BigInteger number = new BigInteger(iban.append("252100").toString());
        int result = 98 - number.mod(BigInteger.valueOf(97)).intValue();
        if (result != Integer.parseInt(accountNr.substring(0, 2)))
            throw new InvalidAccountException();
    }
}
