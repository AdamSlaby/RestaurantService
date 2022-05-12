package pl.restaurant.newsservice.business.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "EMPLOYEE-SERVICE")
public interface EmployeeServiceClient {
    @GetMapping("/exist/{id}")
    boolean isAdminEmployeeExist(@PathVariable("id") Long employeeId);

    @GetMapping("/name/{id}")
    String getEmployeeFullName(@PathVariable("id") Long employeeId);
}
