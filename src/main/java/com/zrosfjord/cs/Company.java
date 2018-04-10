package com.zrosfjord.cs;

import com.zrosfjord.cs.schedule.SchedulesWatcher;
import com.zrosfjord.cs.search.Search;
import com.zrosfjord.cs.search.SearchTermException;
import com.zrosfjord.cs.search.Searchable;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Company implements Searchable, Serializable {

    private String name;
    private LinkedList<Theater> theaters;

    private final SchedulesWatcher watcher;

    /**
     * Constructor for Company and starts a ScheduleWatcher
     *
     * @param name name of the company
     */
    public Company(String name) {
        this.name = name;
        this.theaters = new LinkedList<Theater>();

        this.watcher = new SchedulesWatcher();
    }

    /**
     * Creates a new Theater set up with this company and watcher
     *
     * @param name name of the theater
     * @param location location of the theater
     * @return the new Theater
     */
    public Theater createNewTheater(String name, String location) {
        Theater theater = new Theater(name, location, watcher);
        theaters.add(theater);

        return theater;
    }

    @Override
    public List<Search.Result> search(String question) throws SearchTermException {
        Theater[] array = new Theater[theaters.size()];
        array = theaters.toArray(array);

        return new Search.Query(question).execute(array);
    }


    public String getName() {
        return name;
    }

    public List<Theater> getTheaters() {
        return Collections.unmodifiableList(theaters);
    }

    public SchedulesWatcher getWatcher() {
        return watcher;
    }
}
