package pl.restaurant.restaurantservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.restaurantservice.api.mapper.AddressMapper;
import pl.restaurant.restaurantservice.api.mapper.OpeningHourMapper;
import pl.restaurant.restaurantservice.api.mapper.RestaurantMapper;
import pl.restaurant.restaurantservice.api.mapper.TableMapper;
import pl.restaurant.restaurantservice.api.request.OpeningHour;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.restaurantservice.business.exception.TableNotFoundException;
import pl.restaurant.restaurantservice.data.entity.AddressEntity;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.entity.OpeningHourEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;
import pl.restaurant.restaurantservice.data.repository.AddressRepo;
import pl.restaurant.restaurantservice.data.repository.OpeningHourRepo;
import pl.restaurant.restaurantservice.data.repository.RestaurantRepo;
import pl.restaurant.restaurantservice.data.repository.TableRepo;

import javax.transaction.Transactional;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepo restaurantRepo;
    private AddressRepo addressRepo;
    private OpeningHourRepo openingHourRepo;
    private TableRepo tableRepo;
    private TableService tableService;
    @Override
    public RestaurantInfo getRestaurantInfo(Long id) {
        return RestaurantMapper.mapRestaurantToInfo(restaurantRepo.findById(id)
                .orElseThrow(RestaurantNotFoundException::new));
    }

    @Override
    @Transactional
    public Table getRestaurantTable(int seatsNr, long restaurantId) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepo.findById(restaurantId);
        List<FoodTableEntity> tables = tableRepo.getAllBySeatsNr(seatsNr);
        if (optionalRestaurant.isPresent()) {
            RestaurantEntity restaurant = optionalRestaurant.get();
            Set<FoodTableEntity> restTables = restaurant.getTables();
            for (FoodTableEntity table:tables) {
                assertNotNull(restTables);
                if (!restTables.contains(table)) {
                    restTables.add(table);
                    return TableMapper.mapDataToObject(table);
                }
            }
            FoodTableEntity table = tableService.createTable(seatsNr);
            restTables.add(table);
            return TableMapper.mapDataToObject(table);
        } else {
            FoodTableEntity table = tableService.createTable(seatsNr);
            return TableMapper.mapDataToObject(table);
        }
    }

    @Override
    public Restaurant getRestaurantDetailedInfo(Long id) {
        return RestaurantMapper.mapDataToObject(restaurantRepo
                .findById(id)
                .orElseThrow(RestaurantNotFoundException::new));
    }

    @Override
    public List<RestaurantShortInfo> getAllRestaurants() {
        return restaurantRepo.getAllRestaurants();
    }

    @Override
    @Transactional
    public void addRestaurant(Restaurant restaurant) {
        AddressEntity addressEntity = addressRepo.save(AddressMapper.mapObjectToData(restaurant.getAddress()));
        RestaurantEntity restaurantEntity = restaurantRepo
                .save(RestaurantMapper
                        .mapObjectToData(restaurant, addressEntity));
        openingHourRepo.saveAll(OpeningHourMapper.mapObjectToData(restaurant.getOpeningHours(), restaurantEntity));
        Set<FoodTableEntity> tableSet = new HashSet<>();
        for (Table el:restaurant.getTables()) {
            FoodTableEntity table = tableRepo.findById(el.getId()).orElseThrow(TableNotFoundException::new);
            tableSet.add(table);
        }
        restaurantEntity.setTables(tableSet);
    }

    @Override
    @Transactional
    public void updateRestaurant(Long id, Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepo
                .findById(id)
                .orElseThrow(RestaurantNotFoundException::new);
        restaurantEntity.setPhoneNr(restaurant.getPhoneNr());
        restaurantEntity.setEmail(restaurant.getEmail());
        restaurantEntity.setDeliveryFee(restaurant.getDeliveryFee());
        restaurantEntity.setMinimalDeliveryPrice(restaurant.getMinimalDeliveryPrice());
        AddressEntity address = restaurantEntity.getAddress();
        address.setCity(restaurant.getAddress().getCity());
        address.setStreet(restaurant.getAddress().getStreet());
        address.setHouseNr(restaurant.getAddress().getHouseNr());
        address.setFlatNr(restaurant.getAddress().getFlatNr());
        address.setPostcode(restaurant.getAddress().getPostcode());
        Set<OpeningHourEntity> openingHours = restaurantEntity.getOpeningHourEntities();
        Map<Long, OpeningHour> hours = new HashMap<>();
        restaurant.getOpeningHours().forEach( el -> hours.put(el.getHourId(), el));
        for (OpeningHourEntity hour: openingHours) {
            OpeningHour openingHour = hours.get(hour.getHourId());
            if (openingHour != null) {
                hour.setFromHour(openingHour.getFromHour().toLocalTime());
                hour.setToHour(openingHour.getToHour().toLocalTime());
            }
        }
        restaurantRepo.save(restaurantEntity);
    }

    @Override
    @Transactional
    public void removeTableFromRestaurant(long tableId, long restaurantId) {
        RestaurantEntity restaurant = restaurantRepo
                .findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        Set<FoodTableEntity> tables = restaurant.getTables();
        if (tables != null) {
            for (FoodTableEntity el:tables) {
                if (Objects.equals(el.getTableId(), tableId)) {
                    tables.remove(el);
                    return;
                }
            }
        }
    }
}
