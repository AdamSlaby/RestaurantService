package pl.restaurant.supplyservice.business.calculator;

public interface Creator<T, V> {
    T create(V value1, V value2);
}
