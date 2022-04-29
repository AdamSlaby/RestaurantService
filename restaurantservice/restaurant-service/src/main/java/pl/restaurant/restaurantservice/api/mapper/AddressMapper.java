package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.request.Address;

@UtilityClass
public class AddressMapper {
    public Address mapDataToObject(pl.restaurant.restaurantservice.data.entity.Address address) {
        return new Address().builder()
                .city(address.getCity())
                .street(address.getStreet())
                .houseNr(address.getHouseNr())
                .flatNr(address.getFlatNr())
                .postcode(address.getPostcode())
                .build();
    }
}
