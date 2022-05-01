package pl.restaurant.restaurantservice.business.service;

import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;

public interface TableService {
    FoodTableEntity createTable(int seatsNr);
}
