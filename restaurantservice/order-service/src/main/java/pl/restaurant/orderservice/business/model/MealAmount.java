package pl.restaurant.orderservice.business.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MealAmount implements Comparable<MealAmount> {
    private int mealId;
    private long amount;

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public MealAmount setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public int compareTo(MealAmount mealAmount) {
        if (this.amount > mealAmount.getAmount())
            return -1;
        if (this.amount == mealAmount.getAmount())
            return 0;
        if (this.amount < mealAmount.getAmount())
            return 1;
        throw new RuntimeException("It is not possible");
    }
}
