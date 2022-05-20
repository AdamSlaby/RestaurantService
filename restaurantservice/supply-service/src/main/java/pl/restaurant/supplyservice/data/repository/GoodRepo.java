package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.GoodEntity;

public interface GoodRepo extends JpaRepository<GoodEntity, Long> {

}
