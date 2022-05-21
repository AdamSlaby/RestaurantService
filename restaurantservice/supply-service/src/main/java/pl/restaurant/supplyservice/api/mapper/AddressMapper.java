package pl.restaurant.supplyservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.supplyservice.api.request.Address;
import pl.restaurant.supplyservice.data.entity.AddressEntity;

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

    public static void updateAddress(AddressEntity addressEntity, Address address) {
        addressEntity.setCity(address.getCity());
        addressEntity.setStreet(address.getStreet());
        addressEntity.setHouseNr(address.getHouseNr());
        addressEntity.setFlatNr(address.getFlatNr());
        addressEntity.setPostcode(address.getPostcode());
    }
}
