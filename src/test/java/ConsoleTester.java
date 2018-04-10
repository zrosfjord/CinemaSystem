import com.zrosfjord.cs.Company;
import com.zrosfjord.cs.Movie;
import com.zrosfjord.cs.Screen;
import com.zrosfjord.cs.Theater;
import com.zrosfjord.cs.files.SaveState;
import com.zrosfjord.cs.schedule.ScheduleConflictException;
import com.zrosfjord.cs.schedule.TimeDuration;
import com.zrosfjord.cs.search.Search;
import com.zrosfjord.cs.search.SearchTermException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleTester {

    public static void main(String[] args) {
        Company amc = new Company("AMC");
        amc.getWatcher().start();

        Theater riverdell = amc.createNewTheater("Riverdell Plaza", "1010 Longwind Rd.");
        Screen riverS1 = riverdell.createNewScreen(121, Screen.Format.IMAX);
        Screen riverS2 = riverdell.createNewScreen(130, Screen.Format.NORMAL);

        Theater gardenState = amc.createNewTheater("Garden State Mall", "130 Rt 4");
        Screen gardenS1 = gardenState.createNewScreen(300, Screen.Format.DOLBY);
        Screen gardenS2 = gardenState.createNewScreen(70, Screen.Format.DINE_IN);

        Movie shrek = new Movie("Shrek", Movie.Rating.G, Movie.Format.NORMAL, new TimeDuration(1, 40));
        Movie inception = new Movie("Inception", Movie.Rating.R, Movie.Format.THREE_D, new TimeDuration(2, 30));
        Movie django = new Movie("Django", Movie.Rating.R, Movie.Format.IMAX, new TimeDuration(2, 50));
        Movie incredibles = new Movie("Incredibles", Movie.Rating.PG_13, Movie.Format.NORMAL, new TimeDuration(1, 45));

        try {
            LocalDate today = LocalDate.now();

            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.of(today, LocalTime.of(12, 30)), shrek);
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.of(today, LocalTime.of(14, 30)), inception);
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.of(today, LocalTime.of(17, 20)), django);
            riverS1.getMoviesSchedule().addScheduleable(LocalDateTime.of(today, LocalTime.of(20, 40)), incredibles);

            /*
            riverS2.getMoviesSchedule().addScheduleable(LocalDateTime.now(), inception);
            riverS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(2), inception);
            riverS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(4), django);

            gardenS1.getMoviesSchedule().addScheduleable(LocalDateTime.now(), shrek);
            gardenS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(1), inception);
            gardenS1.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(3), incredibles);

            gardenS2.getMoviesSchedule().addScheduleable(LocalDateTime.now(), django);
            gardenS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(3), inception);
            gardenS2.getMoviesSchedule().addScheduleable(LocalDateTime.now().plusMinutes(5), incredibles);*/
        } catch (ScheduleConflictException ex) {
            ex.printStackTrace();
        }

        SaveState saveState = new SaveState(amc);

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
            } else {
                break;
            }
        }

        amc.getWatcher().setRunning(false);

        try {
            saveState.recordState();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
