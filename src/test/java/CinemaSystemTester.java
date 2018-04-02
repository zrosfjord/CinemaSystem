import com.zrosfjord.cs.Company;
import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.Screen;
import com.zrosfjord.cs.Theater;
import com.zrosfjord.cs.schedule.ScheduleConflictException;
import com.zrosfjord.cs.schedule.TimeDuration;
import com.zrosfjord.cs.search.Search;
import com.zrosfjord.cs.search.SearchTerm;
import com.zrosfjord.cs.search.SearchTermException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CinemaSystemTester {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Company amc = new Company("AMC");

        Theater riverdell = amc.createNewTheater("Riverdell Plaza", "1010 Longwind Rd.");
        Screen riverS1 = riverdell.createNewScreen(1, 121, Screen.Format.IMAX);
        Screen riverS2 = riverdell.createNewScreen(2, 130, Screen.Format.NORMAL);

        Theater gardenState = amc.createNewTheater("Garden State Mall", "130 Rt 4");
        Screen gardenS1 = gardenState.createNewScreen(1, 300, Screen.Format.DOLBY);
        Screen gardenS2 = gardenState.createNewScreen(2, 70, Screen.Format.DINE_IN);

        Movie shrek = new Movie("Shrek", Movie.Rating.G, Movie.Format.NORMAL, new TimeDuration(0, 1));
        Movie inception = new Movie("Inception", Movie.Rating.R, Movie.Format.THREE_D, new TimeDuration(0, 2));
        Movie django = new Movie("Django", Movie.Rating.R, Movie.Format.IMAX, new TimeDuration(0, 3));
        Movie incredibles = new Movie("Incredibles", Movie.Rating.PG_13, Movie.Format.NORMAL, new TimeDuration(0, 1));

        try {
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.now(), shrek);
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(4), inception);
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(1), django);
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(6), incredibles);

            riverS2.getMoviesSchedule().addScheduleable(LocalDateTime.now(), inception);
            riverS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(2), inception);
            riverS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(4), django);


            gardenS1.getMoviesSchedule().addScheduleable(LocalDateTime.now(), shrek);
            gardenS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(1), inception);
            gardenS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(3), incredibles);

            gardenS2.getMoviesSchedule().addScheduleable(LocalDateTime.now(), django);
            gardenS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(3), inception);
            gardenS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(5), incredibles);
        } catch (ScheduleConflictException ex) {
            ex.printStackTrace();
        }


        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Give a command:");
            String input = scanner.nextLine();
            if(input.startsWith("search")) {
                input = input.replaceFirst("search\\s*", "");

                if(input.isEmpty()){
                    System.out.println("\tDemo search usage: search movie{format:IMAX, name:django, name:incredibles};screen{format:dolby, seats:10};time{hr:12, min:30}");
                    continue;
                }

                List<Search.Result> results = null;

                try {
                    results = amc.search(input);
                } catch (SearchTermException e) {
                    System.out.println(e.getMessage());
                }

                System.out.println("\nSearch Results: "
                        + "\n----------------------\n"
                        + " + "
                        + (results == null || results.isEmpty() ? "Nothing" : results.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n + ")))
                        + "\n----------------------\n");
            }
        }
    }

}
