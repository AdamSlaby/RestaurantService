package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.menuservice.api.response.Type;
import pl.restaurant.menuservice.data.entity.TypeEntity;

import java.util.List;

public interface TypeRepo extends JpaRepository<TypeEntity, Integer> {
    @Query("select new pl.restaurant.menuservice.api.response." +
            "Type(t.typeId, t.name) " +
            "from TypeEntity t")
    List<Type> getAllTypes();

    boolean existsByName(String name);
}
