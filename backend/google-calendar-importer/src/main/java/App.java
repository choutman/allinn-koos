import nl.choutman.allinn.koos.dao.MatchDao;
import nl.choutman.allinn.koos.dao.MatchDaoImpl;
import nl.choutman.allinn.koos.dao.TeamDao;
import nl.choutman.allinn.koos.dao.TeamDaoImpl;
import nl.choutman.allinn.koos.model.Team;
import nl.choutman.allinn.koos.parsers.KoosScheduleParser;
import nl.choutman.allinn.koos.parsers.TeamParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private static Logger logger = LogManager.getLogger();
    static TeamDao teamDao = TeamDaoImpl.getInstance();
    static MatchDao matchDao = MatchDaoImpl.getInstance();
    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    private static void printTeams() {
        System.out.println("Teams:");

        teamDao.getTeams().forEach(System.out::println);
    }

    private static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(dayFormatter);
    }

    private static String formatTime(LocalDateTime dateTime) {
        return dateTime.format(timeFormatter);
    }

    public static void main(String[] args) {

        try {
            final String path = App.class.getResource("nl/choutman/allinn/koos/KoosCompetitie2017.xlsx").getPath();
            TeamParser teamParser = new TeamParser(path);

            teamParser.parseTeams(teamDao::addTeam);

            printTeams();

            KoosScheduleParser koosScheduleParser = new KoosScheduleParser(path);
            koosScheduleParser.parseSchedule(matchDao::addMatch);

            final Team teamDrive = teamDao.findTeam("Drive").get();

            List<String> csvRows = matchDao.getMatches().stream().filter(match -> teamDrive.equals(match.getHomeTeam()) || teamDrive.equals(match.getAwayTeam())).
                    map(match -> {
                        Team homeTeam = match.getHomeTeam();
                        Team awayTeam = match.getAwayTeam();

                        StringBuilder row = new StringBuilder("Koos competitie tegen ");
                        row.append((teamDrive.equals(homeTeam) ? awayTeam : homeTeam).getName());

                        if (homeTeam.isPlayingDoubles() && awayTeam.isPlayingDoubles()) {
                            row.append(" + dubbel");
                        }
                        row.append(",");

                        LocalDateTime matchTime = match.getDateTime();

                        row.append(formatDate(matchTime));
                        row.append(",");
                        row.append(formatTime(matchTime));
                        row.append(",");

                        LocalDateTime endTime = matchTime.plusHours(2);

                        row.append(formatDate(endTime));
                        row.append(",");
                        row.append(formatTime(endTime));
                        row.append(",");

                        row.append("\"All Inn Squash, Vlampijpstraat 79, 3534 AR Utrecht, Netherlands\"");

                        return row.toString();
                    }).collect(Collectors.toList());

            final String header = "Subject,Start Date,Start Time,End Date,End Time,Location";
            csvRows.add(0, header);

            csvRows.stream().forEach(System.out::println);
            File schedule = new File("schedule.csv");

            Files.write(schedule.toPath(), csvRows, Charset.forName("UTF-8"), CREATE, WRITE, TRUNCATE_EXISTING);

            logger.debug("Wrote to {}", schedule.getAbsolutePath());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
