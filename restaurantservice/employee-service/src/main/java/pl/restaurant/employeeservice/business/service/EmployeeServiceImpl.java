package pl.restaurant.employeeservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.restaurant.employeeservice.api.request.*;
import pl.restaurant.employeeservice.api.response.EmployeeInfo;
import pl.restaurant.employeeservice.api.response.EmployeeListView;
import pl.restaurant.employeeservice.api.response.EmployeeShortInfo;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;
import pl.restaurant.employeeservice.business.exception.ColumnNotFoundException;
import pl.restaurant.employeeservice.business.exception.EmployeeNotFoundException;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;
import pl.restaurant.employeeservice.data.repository.EmployeeRepo;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final static int AMOUNT = 10;
    private EmployeeRepo employeeRepo;
    @Override
    public EmployeeListView getEmployees(Filter filter) {
        Pageable pageable = mapSortEventToPageable(filter);
        Page<EmployeeShortInfo> page = employeeRepo.getEmployees(filter.getRestaurantId(), filter.getEmployeeId(),
                filter.getActive(), filter.getSurname(), filter.getWorkstation().toString(),
                pageable);
        return new EmployeeListView().builder()
                .totalElements(page.getTotalElements())
                .employees(page.getContent())
                .build();
    }

    @Override
    @Transactional
    public EmployeeInfo getEmployeeInfo(Long employeeId) {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException());

        return null;
    }

    @Override
    public String logIn(Credentials credentials) {
        return null;
    }

    @Override
    public void logOut() {

    }

    @Override
    public ScheduleInfo addScheduleForEmployee(Schedule schedule) {
        return null;
    }

    @Override
    public Credentials addEmployee(Employee employee) {
        return null;
    }

    @Override
    public void updateEmployeeSchedule(ScheduleInfo scheduleInfo, Long employeeId) {

    }

    @Override
    public void updateEmployee(Employee employee) {

    }

    @Override
    public void dismissEmployee(Long employeeId) {

    }

    private Pageable mapSortEventToPageable(Filter filter) {
        if (filter.getSortEvent() == null) {
            return PageRequest.of(filter.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filter.getSortEvent().getColumn();
                Employee.class.getField(column);
                if (filter.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filter.getPageNr(), AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filter.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filter.getPageNr(), AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filter.getPageNr(), AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }
}
