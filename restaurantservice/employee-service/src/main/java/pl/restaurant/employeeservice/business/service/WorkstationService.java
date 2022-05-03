package pl.restaurant.employeeservice.business.service;

import pl.restaurant.employeeservice.api.response.WorkstationListView;

import java.util.List;

public interface WorkstationService {
    List<WorkstationListView> getAllWorkstation();
}
