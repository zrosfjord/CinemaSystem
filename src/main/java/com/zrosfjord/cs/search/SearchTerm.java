package com.zrosfjord.cs.search;

import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.Screen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum SearchTerm {

    SCREEN_FORMAT("screen-format", Screen.Format.class) {
        @Override
        public Object convert(String s) {
            return Screen.Format.valueOf(s);
        }
    },
    SCREEN_SEATS("screen-seats", Integer.class) {
        @Override
        public Object convert(String s) {
            return Integer.parseInt(s);
        }
    },

    MOVIE_NAME("movie-name", String.class) {
        @Override
        public Object convert(String s) {
            return s;
        }
    },
    MOVIE_RATING("movie-rating", Movie.Rating.class) {
        @Override
        public Object convert(String s) {
            return Movie.Rating.valueOf(s);
        }
    },
    MOVIE_FORMAT("movie-format", Movie.Format.class) {
        @Override
        public Object convert(String s) {
            return Movie.Format.valueOf(s);
        }
    },
    MOVIE_DATE("movie-date", LocalDateTime.class) {
        @Override
        public Object convert(String s) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH.mm");
            return LocalDateTime.parse(s, formatter);
        }
    },

    DURATION_HR("duration-hr", Integer.class) {
        @Override
        public Object convert(String s) {
            return Integer.parseInt(s);
        }
    },
    DURATION_MIN("duration-min", Integer.class) {
        @Override
        public Object convert(String s) {
            return Integer.parseInt(s);
        }
    };


    private final String variableName;
    private final Class termType;

    /**
     * Private Constructor of the SearchTerm enum
     *
     * @param variableName name of variable
     */
    SearchTerm(String variableName, Class termType) {
        this.variableName = variableName;
        this.termType = termType;
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

    public abstract Object convert(String s);

    public String getVariableName() {
        return variableName;
    }

    public Class getTermType() {
        return termType;
    }
}
