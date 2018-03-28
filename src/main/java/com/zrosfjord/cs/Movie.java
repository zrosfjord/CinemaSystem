package com.zrosfjord.cs;

import com.zrosfjord.cs.schedule.Scheduleable;
import com.zrosfjord.cs.schedule.TimeDuration;

public class Movie implements Scheduleable {

    private String name;
    private Rating rating;
    private Format format;

    private TimeDuration duration;

    /**
     * Constructor for Movie
     *
     * @param name name of the movie
     * @param rating rating of the movie
     * @param format format of the movie
     * @param duration duration of the movie
     */
    public Movie(String name, Rating rating, Format format, TimeDuration duration) {
        this.name = name;
        this.rating = rating;
        this.format = format;

        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Movie@{Name: " + name
                + "; Rating: " + rating
                + "; Format: " + format
                + "; Duration: " + duration + ";}";
    }

    @Override
    public TimeDuration getTimeDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public Format getFormat() {
        return format;
    }

    public Rating getRating() {
        return rating;
    }

    public enum Rating {
        G, PG, PG_13, R, NC_17;
    }

    public enum Format {
        NORMAL, THREE_D, IMAX;
    }

}
