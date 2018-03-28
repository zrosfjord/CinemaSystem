package com.zrosfjord.cs;

import com.zrosfjord.cs.schedule.Schedule;
import com.zrosfjord.cs.schedule.ScheduleConflictException;
import com.zrosfjord.cs.schedule.ScheduleObserver;
import com.zrosfjord.cs.schedule.SchedulesWatcher;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Screen implements ScheduleObserver<Movie> {

    private final int id;
    private final int seatCount;
    private Format format;

    private Schedule<Movie> movieSchedule;
    private ConcurrentHashMap<String, Integer> seatsMap;

    private Movie currentMovie;

    /**
     * Constructor for screen class. Sets up the ScheduleWatcher and observer too.
     *
     * @param id screen id
     * @param seatCount amount of seats
     * @param format the format of the room
     * @param watcher the ScheduleWatcher
     */
    public Screen(int id, int seatCount, Format format, final SchedulesWatcher watcher) {
        movieSchedule = new Schedule<Movie>();
        movieSchedule.getObservers().add(this);
        watcher.getSchedules().add(movieSchedule);

        seatsMap = new ConcurrentHashMap<String, Integer>();

        this.id = id;
        this.seatCount = seatCount;
        this.format = format;
    }

    @Override
    public void scheduleUpdated(Schedule<Movie>.ScheduleItem prev, Schedule<Movie>.ScheduleItem current) {
        currentMovie = (current == null ? null : current.getScheduleable());

        Collections.sort(movieSchedule.getItems());

        /*
        System.out.printf("\nScreen Id: %s; Was Playing: %s; Now Playing: %s\n"
                , id
                , prev == null ? "Nothing" : prev.getScheduleable()
                , current == null ? "Nothing" : current.getScheduleable());

        System.out.println(
                "------------------------\n" +
                "Upcoming/Ongoing Flicks: \n" +
                "------------------------");
        System.out.println(" + "
                + movieSchedule.getItems().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n + ")));
        */
    }

    @Override
    public void scheduleItemRemoved(Schedule.ScheduleItem item) {
        seatsMap.remove(item.getId());
    }

    @Override
    public void scheduleItemAdded(Schedule.ScheduleItem item) {
        seatsMap.put(item.getId(), seatCount);
    }

    /**
     * Reserves seats for the customer for a particular show
     *
     * @param id the scheduleItem id
     * @param amount the amount of seats
     * @return pass or fail
     */
    public boolean reserveSeats(String id, int amount) {
        if(!seatsMap.containsKey(id))
            return false;

        if(seatsMap.get(id) - amount < 0)
            return false;

        seatsMap.put(id, seatsMap.get(id) - amount);
        return true;
    }

    @Override
    public String toString() {
        return "Screen@{ID: " + id
                + "; SeatCount: " + seatCount
                + "; Format: " + format
                + "; Schedule: \n" + movieSchedule.getItems().stream()
                .map(e -> " - {Item: " + e.getScheduleable()
                        + "; Seats: " + seatsMap.get(e.getId()) + "/" + seatCount + ";}")
                .limit(5)
                .collect(Collectors.joining("\n"))
                + ";}";
    }


    public int getId() {
        return id;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public Format getFormat() {
        return format;
    }

    public Schedule<Movie> getMoviesSchedule() {
        return movieSchedule;
    }

    public Map<String, Integer> getSeatsMap() {
        return Collections.unmodifiableMap(seatsMap);
    }

    public Movie getCurrentMovie() {
        return currentMovie;
    }

    public enum Format {
        NORMAL, DINE_IN, IMAX, DOLBY
    }

}
