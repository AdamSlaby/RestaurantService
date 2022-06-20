package pl.restaurant.restaurantservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.restaurantservice.api.mapper.TableMapper;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.repository.TableRepo;

@Service
@AllArgsConstructor
public class TableServiceImpl implements TableService {
    private TableRepo tableRepo;

    @Override
    public FoodTableEntity createTable(int seatsNr) {
        if (seatsNr <= 1)
            throw new IllegalArgumentException("Seats number cannot be zero or negative number");
        return tableRepo.save(new FoodTableEntity().builder()
                .seatsNr(seatsNr)
                .build());
    }
}
