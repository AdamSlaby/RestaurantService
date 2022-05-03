package pl.restaurant.employeeservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.employeeservice.api.response.WorkstationListView;
import pl.restaurant.employeeservice.data.repository.WorkstationRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkstationServiceImpl implements WorkstationService {
    private WorkstationRepo workstationRepo;

    @Override
    public List<WorkstationListView> getAllWorkstation() {
        return workstationRepo.getAllWorkstations();
    }
}
