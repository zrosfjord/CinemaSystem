package com.zrosfjord.cs.search;

import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.Screen;
import com.zrosfjord.cs.Theater;
import com.zrosfjord.cs.schedule.Schedule;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Search {

    public static class Query {

        private String question;
        private HashMap<SearchTerm, List<SearchTermItem>> variableMap;

        /**
         * The Query constructor.
         *
         * @param question movie{format:-1, rating:g, name:django, name:shrek}; screen{seats:100, format:ImAx}; time{hr:12, min:20};
         */
        public Query(String question) throws SearchTermException {
            this.question = question;
            variableMap = new HashMap<SearchTerm, List<SearchTermItem>>();

            format();
        }

        /**
         * Takes the string question and assigns it into SearchTerms and SearchTermItems
         *
         * @throws SearchTermException
         */
        private void format() throws SearchTermException {
            // Splits apart the types
            String[] split = question.split(";\\s*");

            // Goes through each type
            for (String str : split) {
                // Grabs the types's string
                Pattern typePat = Pattern.compile("^(?i)(?<type>[^\\{]*)");
                Matcher tMatch = typePat.matcher(str);

                String typeStr = (tMatch.find()) ? tMatch.group("type").trim() : null;

                // Goes through each types variables
                Pattern variablePat = Pattern.compile("(?<variable>\\w+):(?<value>\\w*[^,\\}]*)");
                Matcher vMatch = variablePat.matcher(str);

                // Variable loop
                while (vMatch.find()) {
                    String variableName = typeStr + "-" + vMatch.group("variable").trim();
                    String value = vMatch.group("value").trim().toUpperCase();

                    SearchTerm term = SearchTerm.fromVariableName(variableName);

                    // Creates a new list for the term if it doesn't have one
                    if (!variableMap.containsKey(term)) {
                        variableMap.put(term, new ArrayList<SearchTermItem>());
                    }

                    // Used for enums
                    List<String> valueList = new ArrayList<String>();

                    if (term.getVariableType().isEnum()) {

                        // Puts enum values in a string list
                        Enum<?>[] enumb = (Enum<?>[]) term.getVariableType().getEnumConstants();
                        List<String> enumbList = Arrays.stream(enumb).map(Enum::name).collect(Collectors.toList());

                        // If value is -1 all values get inserted, else makes sure the enum value is real
                        if (value.equalsIgnoreCase("-1")) {
                            valueList.addAll(enumbList);
                        } else if (!enumbList.contains(value)) {
                            throw new SearchTermException(variableName + ":" + value);
                        } else {
                            valueList.add(value);
                        }
                    } else {
                        valueList.add(value);
                    }

                    // Adds all values to variableMap
                    valueList.stream().forEach(listItem -> {
                        variableMap.get(term).add(new SearchTermItem(term, listItem.toUpperCase()));
                    });
                }
            }
        }

        /**
         * Executes the search
         *
         * @param theaters the theaters it is searching
         * @return A list of results
         */
        public List<Result> execute(Theater... theaters) {
            System.out.println("\nSearch Terms: ");
            System.out.println(variableMap.entrySet().stream()
                    .map(i -> "Term: " + i.getKey()
                            + "; Variables: " + i.getValue().stream()
                            .map(e -> e.getItem() + "").collect(Collectors.joining(", ")))
                    .collect(Collectors.joining("\n")));


            LinkedList<Result> results = new LinkedList<Result>();

            // Goes through theaters
            for (Theater t : theaters) {

                // Goes through each theater's screens
                Iterator<Screen> screenIterator = t.getScreensMap().values().iterator();
                while (screenIterator.hasNext()) {
                    Screen nextScreen = screenIterator.next();

                    // Left up here for effeciency
                    if (!meetsTerm(SearchTerm.SCREEN_FORMAT, nextScreen.getFormat()))
                        continue;

                    // Goes through the movies schedule in each screen
                    Iterator<Schedule<Movie>.ScheduleItem> itemIterator = nextScreen.getMoviesSchedule().getItems().iterator();
                    while (itemIterator.hasNext()) {
                        Schedule<Movie>.ScheduleItem nextItem = itemIterator.next();

                        Integer seats = nextScreen.getSeatsMap().get(nextItem.getId());
                        Movie m = nextItem.getScheduleable();

                        // Easier to run a function and convert to a boolean list than to do individually
                        Map<SearchTerm, Object> map = new HashMap<SearchTerm, Object>() {{
                            put(SearchTerm.SCREEN_SEATS, seats);
                            put(SearchTerm.MOVIE_FORMAT, m.getFormat());
                            put(SearchTerm.MOVIE_RATING, m.getRating());
                            put(SearchTerm.MOVIE_NAME, m.getName());
                            put(SearchTerm.MOVIE_HRS, m.getTimeDuration().getHours());
                            put(SearchTerm.MOVIE_MINS, m.getTimeDuration().getMinutes());
                            put(SearchTerm.TIME_HR, nextItem.getStartTime().getHour());
                            put(SearchTerm.TIME_MIN, nextItem.getStartTime().getMinute());
                        }};

                        // Runs meetsTerm on each entry and stores results
                        List<Boolean> bList = map.entrySet().stream()
                                .map(entry -> (boolean) meetsTerm(entry.getKey(), entry.getValue()))
                                .collect(Collectors.toList());

                        // Checks if any qualifier failed
                        if (bList.contains(false)) {
                            continue;
                        }

                        results.add(new Result(t, nextScreen, nextItem));
                    }
                }
            }

            return results;
        }

        /**
         * Makes sure it meets the Search Term
         *
         * @param term the serach term being tested
         * @param obj  the objected tested
         * @return pass fail result
         */
        public boolean meetsTerm(SearchTerm term, Object obj) {
            if (variableMap.containsKey(term)) {

                // Runs each searchTermItem's function for this searchTerm
                Iterator<SearchTermItem> iterator = variableMap.get(term).iterator();
                while (iterator.hasNext()) {
                    SearchTermItem next = iterator.next();

                    // If it passes it, it returns true.
                    if ((boolean) next.getFunction().apply(next.getItem(), obj))
                        return true;
                }

                // Fails if none of the SearchTimeItems pass
                return false;
            }

            // Passes if the variable map doesn't contain the searchterm
            return true;
        }

    }

    public static class Result {

        private Theater theater;
        private Screen screen;
        private Schedule<Movie>.ScheduleItem item;

        /**
         * Private Constructor. Only Search.Query can call it
         *
         * @param theater      The theater
         * @param screen       The screen
         * @param scheduleItem The schedule item
         */
        private Result(Theater theater, Screen screen, Schedule.ScheduleItem scheduleItem) {
            this.theater = theater;
            this.screen = screen;
            this.item = scheduleItem;
        }

        @Override
        public String toString() {
            return "Search.Result@{Item: " + item
                    + "; Seats: " + screen.getSeatsMap().get(item.getId()) + "/" + screen.getSeatCount()
                    + "; Theater: " + theater.getName()
                    + ";}";
        }

        public Theater getTheater() {
            return theater;
        }

        public Screen getScreen() {
            return screen;
        }

        public Schedule<Movie>.ScheduleItem getItem() {
            return item;
        }

    }

}
