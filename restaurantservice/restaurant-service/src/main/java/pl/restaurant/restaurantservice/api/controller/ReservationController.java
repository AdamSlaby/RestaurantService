package pl.restaurant.restaurantservice.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.restaurantservice.api.request.Reservation;
import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.api.response.ReservationInfo;
import pl.restaurant.restaurantservice.api.response.ReservationShortInfo;
import pl.restaurant.restaurantservice.business.service.ReservationService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reservation")
@Log4j2
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    @GetMapping("/info/{id}")
    public ReservationInfo getReservationInfo(@PathVariable("id") Long reservationId) {
        return reservationService.getReservationInfo(reservationId);
    }

    @GetMapping("/list")
    public List<ReservationShortInfo> getReservations(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime dateTime,
                                                      @RequestParam(value = "restaurant", required = false)
                                                      Long restaurantId) {
        return reservationService.getReservations(dateTime, restaurantId);
    }

    @GetMapping("/hours")
    public List<AvailableHour> getAvailableHours(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     LocalDateTime dateTime,
                                                 @RequestParam("people") int peopleNr,
                                                 @RequestParam("restaurant") Long restaurantId) {
        return reservationService.getAvailableHours(restaurantId, dateTime, peopleNr);
    }

    @PostMapping("/")
    public void addReservation(@RequestBody @Valid Reservation reservation) {
        reservationService.addReservation(reservation);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable("id") Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }
}
