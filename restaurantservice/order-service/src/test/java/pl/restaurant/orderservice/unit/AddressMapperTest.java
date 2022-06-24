package pl.restaurant.orderservice.unit;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import pl.restaurant.orderservice.api.mapper.AddressMapper;
import pl.restaurant.orderservice.api.request.Address;
import pl.restaurant.orderservice.data.entity.AddressEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddressMapperTest {

    @Test
    public void mapDataToObjectSuccess() {
        //given
        AddressEntity addressEntity = new EasyRandom(getParameters()).nextObject(AddressEntity.class);
        //when
        Address address = AddressMapper.mapDataToObject(addressEntity);
        //then
        assertNotNull(address);
        assertThat(address.getCity()).isEqualTo(addressEntity.getCity());
        assertThat(address.getStreet()).isEqualTo(addressEntity.getStreet());
        assertThat(address.getHouseNr()).isEqualTo(addressEntity.getHouseNr());
        assertThat(address.getFlatNr()).isEqualTo(addressEntity.getFlatNr());
        assertThat(address.getPostcode()).isEqualTo(addressEntity.getPostcode());
    }

    @Test
    public void mapObjectToDataSuccess() {
        //given
        Address address = new EasyRandom(getParameters()).nextObject(Address.class);
        //when
        AddressEntity addressEntity = AddressMapper.mapObjectToData(address);
        //then
        assertNotNull(addressEntity);
        assertNull(addressEntity.getAddressId());
        assertThat(addressEntity.getCity()).isEqualTo(address.getCity());
        assertThat(addressEntity.getStreet()).isEqualTo(address.getStreet());
        assertThat(addressEntity.getHouseNr()).isEqualTo(address.getHouseNr());
        assertThat(addressEntity.getFlatNr()).isEqualTo(address.getFlatNr());
        assertThat(addressEntity.getPostcode()).isEqualTo(address.getPostcode());
        assertNull(addressEntity.getOrder());
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
