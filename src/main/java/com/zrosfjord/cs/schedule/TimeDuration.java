package com.zrosfjord.cs.schedule;

import java.time.LocalDateTime;

public class TimeDuration {

    private int hrs, mins;

    /**
     * Constructor for TimeDuration
     *
     * @param hrs hours
     * @param mins minutes
     */
    public TimeDuration(int hrs, int mins) {
        this.hrs = hrs;
        this.mins = mins;
    }

    /**
     * Checks whether there is a conflict at a given start time
     *
     * @param start start time
     * @param test test time
     * @return true if there is a conflict. false otherwise.
     */
    public boolean conflictsAt(LocalDateTime start, LocalDateTime test) {
        LocalDateTime end = start.plusHours(hrs);
        end = end.plusMinutes(mins);

        return (start.isBefore(test) && end.isAfter(test));
    }

    @Override
    public String toString() {
        return "TimeDuration@{Hrs: " + hrs + "; Mins: " + mins + ";}";
    }


    // Getters

    public int getHours() {
        return hrs;
    }

    public int getMinutes() {
        return mins;
    }

}
