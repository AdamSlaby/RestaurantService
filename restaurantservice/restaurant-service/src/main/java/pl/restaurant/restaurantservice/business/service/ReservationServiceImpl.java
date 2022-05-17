package pl.restaurant.restaurantservice.business.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.restaurant.restaurantservice.api.mapper.ReservationMapper;
import pl.restaurant.restaurantservice.api.request.Reservation;
import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.api.response.ReservationInfo;
import pl.restaurant.restaurantservice.api.response.ReservationShortInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.business.exception.CannotDeleteReservationException;
import pl.restaurant.restaurantservice.business.exception.ReservationAlreadyExistsException;
import pl.restaurant.restaurantservice.business.exception.ReservationNotFoundException;
import pl.restaurant.restaurantservice.business.templetemethod.AvailableHoursForLargeGroupTemplate;
import pl.restaurant.restaurantservice.business.templetemethod.AvailableHoursForSmallGroupTemplate;
import pl.restaurant.restaurantservice.business.templetemethod.AvailableHoursTemplate;
import pl.restaurant.restaurantservice.data.entity.*;
import pl.restaurant.restaurantservice.data.repository.ReservationRepo;
import pl.restaurant.restaurantservice.data.repository.ReservationTableRepo;
import pl.restaurant.restaurantservice.data.repository.RestaurantRepo;
import pl.restaurant.restaurantservice.data.repository.RestaurantTableEntityRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepo reservationRepo;
    private ReservationTableRepo reservationTableRepo;
    private RestaurantTableEntityRepo restaurantTableRepo;
    private RabbitTemplate rabbitTemplate;
    private RestaurantRepo restaurantRepo;

    @Override
    public ReservationInfo getReservationInfo(Long reservationId) {
        ReservationEntity reservation = reservationRepo.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
        Set<ReservationTableEntity> tables = reservation.getTables();
        return ReservationMapper.mapDataToInfo(reservation, tables);
    }

    @Override
    public List<ReservationShortInfo> getReservations(LocalDateTime dateTime, Long restaurantId) {
        LocalDateTime from = dateTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = dateTime.withHour(23).withMinute(59).withSecond(59);
        List<ReservationEntity> reservations = reservationTableRepo.getReservationsFromDate(from, to, restaurantId);
        return reservations.stream()
                .map(ReservationMapper::mapDataToShosrtInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<AvailableHour> getAvailableHours(Long restaurantId, LocalDateTime dateTime, int peopleNr) {
        Set<FoodTableEntity> tables = new LinkedHashSet<>(restaurantTableRepo.getAllByRestaurantId(restaurantId));
        LocalDateTime from = dateTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = dateTime.withHour(23).withMinute(59).withSecond(59);
        List<ReservationEntity> reservations = reservationRepo.getReservationsFromDate(from, to);
        AvailableHoursTemplate template;
        if (peopleNr <= 5)
            template = new AvailableHoursForSmallGroupTemplate();
        else
            template = new AvailableHoursForLargeGroupTemplate();
        return template.getAvailableHours(tables, dateTime, reservations, peopleNr);
    }

    @Override
    @Transactional
    public void addReservation(Reservation reservation) {
        if (reservationTableRepo.isReservationExists(reservation.getTableIds(), reservation.getFromHour()) != 0)
            throw new ReservationAlreadyExistsException();
        ReservationEntity save = reservationRepo.save(ReservationMapper.mapObjectToData(reservation));
        for (Long id : reservation.getTableIds()) {
            reservationTableRepo.save(new ReservationTableEntity().builder()
                    .reservationTableId(new ReservationTableId(save.getReservationId(),
                            new RestaurantTableId(reservation.getRestaurantId(), id)))
                    .reservation(save)
                    .restaurantTable(restaurantTableRepo.getById(new RestaurantTableId(reservation.getRestaurantId(), id)))
                    .build());
        }
        Optional<RestaurantShortInfo> restaurant = restaurantRepo.getRestaurant(reservation.getRestaurantId());
        restaurant.ifPresent(restaurantShortInfo -> rabbitTemplate.convertAndSend("reservation",
                ReservationMapper.mapDataToEmailInfo(save, reservation, restaurantShortInfo)));
    }

    @Override
    @Transactional
    public void deleteReservation(Long reservationId) {
        LocalDateTime fromHour = reservationRepo.getReservationFromHour(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
        if (LocalDateTime.now().isAfter(fromHour))
            throw new CannotDeleteReservationException();
        reservationTableRepo.deleteAllByReservationId(reservationId);
        reservationRepo.deleteById(reservationId);
    }
}
