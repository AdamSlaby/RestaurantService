package pl.restaurant.restaurantservice.business.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Service;
import pl.restaurant.restaurantservice.api.mapper.*;
import pl.restaurant.restaurantservice.api.request.OpeningHour;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.restaurantservice.business.exception.TableNotFoundException;
import pl.restaurant.restaurantservice.data.entity.*;
import pl.restaurant.restaurantservice.data.repository.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@Log4j2
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepo restaurantRepo;
    private AddressRepo addressRepo;
    private OpeningHourRepo openingHourRepo;
    private TableRepo tableRepo;
    private RestaurantTableEntityRepo restaurantTableRepo;
    private TableService tableService;
    @Override
    @Transactional
    public RestaurantInfo getRestaurantInfo(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepo.findById(id)
                .orElseThrow(RestaurantNotFoundException::new);
        List<OpeningHour> hours = OpeningHourMapper.mapDataToObject(openingHourRepo.getHoursByRestaurant(id));
        log.log(Level.INFO, hours.toString());
        return RestaurantMapper.mapRestaurantToInfo(restaurantEntity, hours);
    }

    @Override
    public boolean isRestaurantExist(Long id) {
        return restaurantRepo.existsByRestaurantId(id);
    }

    @Override
    @Transactional
    public Table getRestaurantTable(int seatsNr, long restaurantId) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepo.findById(restaurantId);
        List<FoodTableEntity> tables = tableRepo.getAllBySeatsNr(seatsNr);
        if (optionalRestaurant.isPresent()) {
            RestaurantEntity restaurant = optionalRestaurant.get();
            List<FoodTableEntity> restTables = restaurant.getRestaurantTables()
                    .stream()
                    .map(RestaurantTableEntity::getTable)
                    .collect(Collectors.toList());
            for (FoodTableEntity table:tables) {
                assertNotNull(restTables);
                if (!restTables.contains(table)) {
                    restaurantTableRepo.save(RestaurantTableMapper.mapToData(restaurant, table));
                    return TableMapper.mapDataToObject(table);
                }
            }
            FoodTableEntity table = tableService.createTable(seatsNr);
            restaurantTableRepo.save(RestaurantTableMapper.mapToData(restaurant, table));
            return TableMapper.mapDataToObject(table);
        } else {
            FoodTableEntity table = tableService.createTable(seatsNr);
            return TableMapper.mapDataToObject(table);
        }
    }

    @Override
    public Restaurant getRestaurantDetailedInfo(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepo
                .findById(id)
                .orElseThrow(RestaurantNotFoundException::new);
        List<OpeningHour> hours = OpeningHourMapper.mapDataToObject(openingHourRepo.getHoursByRestaurant(id));
        return RestaurantMapper.mapDataToObject(restaurantEntity, hours);
    }

    @Override
    public RestaurantShortInfo getRestaurantShortInfo(Long restaurantId) {
        return restaurantRepo.getRestaurant(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
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
        for (Table el:restaurant.getTables()) {
            FoodTableEntity table = tableRepo.findById(el.getId()).orElseThrow(TableNotFoundException::new);
            restaurantTableRepo.save(RestaurantTableMapper.mapToData(restaurantEntity, table));
        }
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
        restaurantTableRepo.deleteById(new RestaurantTableId(restaurantId, tableId));
    }

    @Override
    public boolean isRestaurantTableExist(Long restaurantId, Long tableId) {
        return restaurantTableRepo.existsById(new RestaurantTableId(restaurantId, tableId));
    }
}
