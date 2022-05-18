package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.response.Type;

import java.util.List;

public interface TypeService {
    List<Type> getAllTypes();

    Type addType(String type);

    void updateType(Integer typeId, String type);
}
