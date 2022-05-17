package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.menuservice.data.entity.TypeEntity;

public interface TypeRepo extends JpaRepository<TypeEntity, Integer> {
}
