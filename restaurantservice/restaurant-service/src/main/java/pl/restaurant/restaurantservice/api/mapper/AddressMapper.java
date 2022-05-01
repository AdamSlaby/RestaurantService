package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.request.Address;
import pl.restaurant.restaurantservice.data.entity.AddressEntity;

@UtilityClass
public class AddressMapper {
    public Address mapDataToObject(AddressEntity addressEntity) {
        return new Address().builder()
                .city(addressEntity.getCity())
                .street(addressEntity.getStreet())
                .houseNr(addressEntity.getHouseNr())
                .flatNr(addressEntity.getFlatNr())
                .postcode(addressEntity.getPostcode())
                .build();
    }

    public AddressEntity mapObjectToData(Address address) {
        return new AddressEntity().builder()
                .city(address.getCity())
                .street(address.getStreet())
                .houseNr(address.getHouseNr())
                .flatNr(address.getFlatNr())
                .postcode(address.getPostcode())
                .build();
    }
}
