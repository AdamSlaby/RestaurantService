package pl.restaurant.restaurantservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.restaurant.restaurantservice.api.mapper.AddressMapper;
import pl.restaurant.restaurantservice.api.mapper.OpeningHourMapper;
import pl.restaurant.restaurantservice.api.mapper.RestaurantMapper;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.restaurantservice.business.exception.TableNotFoundException;
import pl.restaurant.restaurantservice.data.entity.AddressEntity;
import pl.restaurant.restaurantservice.data.entity.FoodTable;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;
import pl.restaurant.restaurantservice.data.repository.AddressRepo;
import pl.restaurant.restaurantservice.data.repository.OpeningHourRepo;
import pl.restaurant.restaurantservice.data.repository.RestaurantRepo;
import pl.restaurant.restaurantservice.data.repository.TableRepo;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepo restaurantRepo;
    private AddressRepo addressRepo;
    private OpeningHourRepo openingHourRepo;
    private TableRepo tableRepo;
    @Override
    public RestaurantInfo getRestaurantInfo(Long id) {
        return RestaurantMapper.mapRestaurantToInfo(restaurantRepo.findById(id)
                .orElseThrow(RestaurantNotFoundException::new));
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
        Set<FoodTable> tableSet = new HashSet<>();
        for (Table el:restaurant.getTables()) {
            FoodTable table = tableRepo.findById(el.getId()).orElseThrow(TableNotFoundException::new);
            tableSet.add(table);
        }
        restaurantEntity.setTables(tableSet);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        return null;
    }
}
