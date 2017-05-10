import com.google.common.io.Files;
import nl.choutman.allinn.koos.model.Match;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by choutman on 10/05/2017.
 */
public class AppTest {

    @Before
    public void runApp() throws Exception {
        App.main(null);
    }

    @Test
    public void testCsvFileIsCreated() throws Exception {
        File schedule = new File("schedule.csv");

        Assert.assertTrue(schedule.exists());
    }

    @Test
    public void testScheduleContainsCorrectHeader() throws Exception {
        File schedule = new File("schedule.csv");
        String header = Files.readFirstLine(schedule, Charset.forName("UTF-8"));

        assertEquals("Subject,Start Date,Start Time,End Date,End Time,Location", header);
    }

    @Test
    public void testMatchesForTeamDriveIsEqualToTheNumberOfRowsInTheCSV() throws Exception {
        List<Match> expectedMatches = App.matchDao.getMatches().stream().filter(match -> match.getHomeTeam().getName().equals("Drive")
                || match.getAwayTeam().getName().equals("Drive")).collect(Collectors.toList());

        File schedule = new File("schedule.csv");
        List<String> matchSchedule = Files.readLines(schedule, Charset.forName("UTF-8"));
        matchSchedule.remove(0);

        assertEquals(expectedMatches.size(), matchSchedule.size());
    }
}