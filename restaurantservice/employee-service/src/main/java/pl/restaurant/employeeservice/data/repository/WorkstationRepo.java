package pl.restaurant.employeeservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.employeeservice.api.response.WorkstationListView;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;

import java.util.List;

public interface WorkstationRepo extends JpaRepository<WorkstationEntity, Integer> {
    @Query("select new pl.restaurant.employeeservice.api.response.WorkstationListView(w.workstationId, w.name) " +
            "from WorkstationEntity w")
    List<WorkstationListView> getAllWorkstations();
}
