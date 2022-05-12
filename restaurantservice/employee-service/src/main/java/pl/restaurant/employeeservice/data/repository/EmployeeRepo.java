package pl.restaurant.employeeservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.employeeservice.api.response.EmployeeShortInfo;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;

import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Long> {
    @Query("select new pl.restaurant.employeeservice.api.response" +
            ".EmployeeShortInfo(e.employeeId, e.name, e.surname, e.workstation.workstationId) " +
            "from EmployeeEntity e " +
            "where (:rId is null or e.restaurantId = :rId) and " +
            "(:eId is null or e.employeeId = :eId) and " +
            "(:active is null or e.active = :active) and " +
            "(:surname is null or e.surname like %:surname%) and " +
            "(:wId is null or e.workstation.workstationId = :wId)")
    Page<EmployeeShortInfo> getEmployees(@Param("rId") Long restaurantId,
                                         @Param("eId") Long employeeId,
                                         @Param("active") Boolean active,
                                         @Param("surname") String surname,
                                         @Param("wId") Integer workstationId,
                                         Pageable pageable);

    @Query("select e.restaurantId from EmployeeEntity e where e.employeeId = :id")
    Optional<Long> getEmployeeRestaurantId(@Param("id") Long employeeId);

    boolean existsByEmployeeIdAndWorkstationName(Long employeeId, String workstationName);

    @Query("select concat(e.name, ' ', e.surname) from EmployeeEntity e where e.employeeId = :id")
    Optional<String> getEmployeeFullNameById(@Param("id") Long employeeId);
}
