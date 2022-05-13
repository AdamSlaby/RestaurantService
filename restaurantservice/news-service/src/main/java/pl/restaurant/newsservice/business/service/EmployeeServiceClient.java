package pl.restaurant.newsservice.business.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "EMPLOYEE-SERVICE")
public interface EmployeeServiceClient {
    @GetMapping("/exist/{id}")
    boolean isAdminEmployeeExist(@PathVariable("id") Long employeeId);

    @GetMapping("/name/{id}")
    String getEmployeeFullName(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                               @PathVariable("id") Long employeeId);
}
