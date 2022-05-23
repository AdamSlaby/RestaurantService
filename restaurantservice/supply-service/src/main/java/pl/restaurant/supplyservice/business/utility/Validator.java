package pl.restaurant.supplyservice.business.utility;

import lombok.experimental.UtilityClass;
import pl.restaurant.supplyservice.business.exception.InvalidNipException;

@UtilityClass
public class Validator {
    public void validateNip(String nip, String fieldName) {
        int[] wages = {6, 5, 7, 2, 3, 4, 5, 6, 7};
        int sum = 0;
        for (int i = 0; i < nip.length() - 1; i++)
            sum += Integer.parseInt(String.valueOf(nip.charAt(i))) * wages[i];
        if (sum % 11 != Integer.parseInt(String.valueOf(nip.charAt(nip.length() - 1))))
            throw new InvalidNipException(fieldName);
    }
}
