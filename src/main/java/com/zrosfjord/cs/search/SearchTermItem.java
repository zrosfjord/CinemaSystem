package com.zrosfjord.cs.search;

import com.zrosfjord.cs.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SearchTermItem<T> {

    private SearchTerm term;
    private T item;
    private String rawItem;

    private BiFunction<T, T, Boolean> function;

    /**
     * Constructor for SearchTermItem
     *
     * @param term SearchTerm
     * @param item String version of item
     */
    public SearchTermItem(SearchTerm term, String item)  {
        this.term = term;
        this.rawItem = item;

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
        Method m = term.getConversionMethod();
        if(m != null)
            this.item = (T) ReflectionUtils.invokeMethod(m, null, rawItem.toUpperCase());
        else
            this.item = (T) rawItem;

        if(term.getVariableType().equals(Integer.class)) {
            function = (a, b) -> {
                return (Integer) a <= (Integer) b;
            };
        } else if(term.getVariableType().equals(String.class)) {
            function = (a, b) -> {
                return ((String) a).equalsIgnoreCase((String) b);
            };
        } else {
            function = (a,b) -> {
                return a == b;
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
