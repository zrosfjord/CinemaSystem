package com.zrosfjord.cs.search;

import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.Screen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum SearchTerm {

    SCREEN_FORMAT("screen-format", Screen.Format.class, "valueOf"),
    SCREEN_SEATS("screen-seats", Integer.class, "valueOf"),

    MOVIE_NAME("movie-name", String.class, null),
    MOVIE_RATING("movie-rating", Movie.Rating.class, "valueOf"),
    MOVIE_FORMAT("movie-format", Movie.Format.class, "valueOf"),

    TIME_HR("time-hr", Integer.class, "parseInt"),
    TIME_MIN("time-min", Integer.class, "parseInt");


    private final String variableName;
    private final Class<?> variableType;
    private final String methodName;

    /**
     * Private Constructor of the SearchTerm enum
     *
     * @param variableName name of variable
     * @param type class type
     * @param method name of the method that will transfrom the string to its target object
     * @param <T> generic param
     */
    <T> SearchTerm(String variableName, Class<T> type, String method) {
        this.variableName = variableName;
        this.variableType = type;
        this.methodName = method;
    }

    /**
     * Gives the SearchTerm associated with a string
     *
     * @param variableName the String
     * @return the SearchTerm
     * @throws SearchTermException if there is no search term
     */
    public static SearchTerm fromVariableName(String variableName) throws SearchTermException {
        variableName = variableName.trim();

        for(SearchTerm t : SearchTerm.values()) {
            if (t.variableName.equalsIgnoreCase(variableName))
                return t;
        }

        throw new SearchTermException(variableName);
    }

    /**
     * Gets the conversion method
     *
     * @return Method to convert string to variableType
     * @throws NoSuchMethodException if the method doesn't exist
     */
    public Method getConversionMethod() throws NoSuchMethodException {
        if(methodName == null)
            return null;

        Method m = variableType.getDeclaredMethod(methodName, String.class);
        m.setAccessible(true);
        return m;
    }


    public String getVariableName() {
        return variableName;
    }

    public Class<?> getVariableType() {
        return variableType;
    }

    public String getConversionMethodName() {
        return methodName;
    }

}
