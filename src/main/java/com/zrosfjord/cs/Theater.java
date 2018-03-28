package com.zrosfjord.cs;

import com.zrosfjord.cs.schedule.SchedulesWatcher;
import com.zrosfjord.cs.search.Search;
import com.zrosfjord.cs.search.SearchTermException;
import com.zrosfjord.cs.search.Searchable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Theater implements Searchable {

    private String name, location;
    private final SchedulesWatcher watcher;

    private ConcurrentHashMap<Integer, Screen> screensMap;

    /**
     * Theater Constructor
     *
     * @param name name of the theater
     * @param location location of the theater
     * @param watcher
     */
    public Theater(String name, String location, final SchedulesWatcher watcher) {
        this.name = name;
        this.location = location;

        this.watcher = watcher;

        screensMap = new ConcurrentHashMap<Integer, Screen>();
    }

    /**
     * Creates a new Screen registered with this theater
     *
     * @param id the id of the screen
     * @param seatCount the amount of seats
     * @param format the format of the screen
     * @return the new Screen
     */
    public Screen createNewScreen(int id, int seatCount, Screen.Format format) {
        Screen screen = new Screen(id, seatCount, format, watcher);
        screensMap.put(id, screen);
        screensMap = screensMap.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (oldValue, newValue) -> oldValue, ConcurrentHashMap::new));

        return screen;
    }

    @Override
    public List<Search.Result> search(String question) throws SearchTermException {
        return new Search.Query(question).execute(this);
    }

    @Override
    public String toString() {
        return "Theater@{Name: " + name
                + "; Location: " + location
                + "; Screens: " + screensMap.entrySet().stream()
                .map(e -> "\n - {ID: " + e.getKey() + "; Now Playing: "
                        + (e.getValue().getCurrentMovie() == null ? "Nothing" : e.getValue().getCurrentMovie())
                        + ";}")
                .limit(10)
                .collect(Collectors.joining())
                + "\n;}";
    }


    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Map<Integer, Screen> getScreensMap() {
        return Collections.unmodifiableMap(screensMap);
    }

}
