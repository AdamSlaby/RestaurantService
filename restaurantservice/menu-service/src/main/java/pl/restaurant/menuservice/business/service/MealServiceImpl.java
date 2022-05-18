package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.request.SortDirection;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.api.response.MealShortInfo;
import pl.restaurant.menuservice.business.exception.ColumnNotFoundException;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.repository.MealRepo;

@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService {
    private final static int AMOUNT = 10;
    private MealRepo mealRepo;

    @Override
    public MealListView getMeals(MealFilters filters) {
        Pageable pageable = mapSortEventToPageable(filters);
        Page<MealShortInfo> page = mealRepo.getMeals(filters.getMealName(), filters.getTypeId(), pageable);
        return new MealListView(page.getTotalElements(), page.getContent());
    }

    private Pageable mapSortEventToPageable(MealFilters filters) {
        if (filters.getSortEvent() == null) {
            return PageRequest.of(filters.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filters.getSortEvent().getColumn();
                MealEntity.class.getDeclaredField(column);
                if (filters.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filters.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }
}
