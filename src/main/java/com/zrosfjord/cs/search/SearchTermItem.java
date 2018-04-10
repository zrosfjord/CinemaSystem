package com.zrosfjord.cs.search;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

public class SearchTermItem<T> {

    private SearchTerm term;
    private T item;

    private BiFunction<T, T, Boolean> function;

    /**
     * Constructor for SearchTermItem
     *
     * @param term SearchTerm
     * @param item String version of item
     */
    public SearchTermItem(SearchTerm term, String item)  {
        this.term = term;
        this.item = (T) term.convert(item);

        init();
    }

    /**
     * Sets the item to the right type, and selects the correct function.
     *
     * @throws NoSuchMethodException if there are issues converting
     * @throws InvocationTargetException if there are issues converting
     * @throws IllegalAccessException if there are issues converting
     */
    private void init() {
        if (item instanceof Integer) {
            function = (a, b) -> {
                return (Integer) a <= (Integer) b;
            };
        } else if (item instanceof String) {
            function = (a, b) -> {
                return ((String) a).equalsIgnoreCase((String) b);
            };
        } else if (item instanceof LocalDateTime) {
            function = (a, b) -> {
                LocalDateTime aTime = (LocalDateTime) a;
                LocalDateTime bTime = (LocalDateTime) b;

                boolean day = aTime.toLocalDate().isEqual(bTime.toLocalDate());
                return day && aTime.isBefore(bTime);
            };
        } else {
            function = (a, b) -> {
                return a.equals(b);
            };
        }
    }

    public SearchTerm getTerm() {
        return term;
    }

    public T getItem() {
        return item;
    }

    public BiFunction<T, T, Boolean> getFunction() {
        return function;
    }

}
