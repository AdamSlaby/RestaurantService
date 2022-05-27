package pl.restaurant.mailservice.api.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String houseNr;
    private String flatNr;
    private String postcode;

    @Override
    public String toString() {
        return city + " ul. " + street + " " + houseNr + "/" + flatNr + " " + postcode;
    }
}
