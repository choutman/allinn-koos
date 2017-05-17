package nl.choutman.allinn.koos.parsers;

import nl.choutman.allinn.koos.dao.TeamDao;
import nl.choutman.allinn.koos.dao.TeamDaoImpl;
import nl.choutman.allinn.koos.model.Match;
import nl.choutman.allinn.koos.model.Team;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

public class KoosScheduleParser extends AbstractExcelParser {

  private final TeamDao teamDao;
  private LocalDateTime dateTime = null;

  private int parsed = 0;
  private int error = 0;

  public KoosScheduleParser(String path) throws Exception {
    super(path);

    teamDao = TeamDaoImpl.getInstance();
  }

  public void parseSchedule(Consumer<Match> matchConsumer) {

    parseRows(23, 244, row -> {

      Cell firstColumn = row.getCell(0);
      Cell secondColumn = row.getCell(1);

      if (isDateHeader(firstColumn)) {
        Date date = firstColumn.getDateCellValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.of("Europe/Paris")).toLocalDate();

        String timeString = secondColumn.getStringCellValue();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm 'uur'");
        LocalTime time = LocalTime.parse(timeString, timeFormatter);

        dateTime = localDate.atTime(time);

        System.out.println("Set time to: " + dateTime);
      } else if (firstColumn != null) {
        try {
          final String homeTeamString = firstColumn.getStringCellValue();
          final String awayTeamString = secondColumn.getStringCellValue();

          Optional<Team> homeTeam = teamDao.findTeam(homeTeamString);
          Optional<Team> awayTeam = teamDao.findTeam(awayTeamString);

          if (homeTeam.isPresent() && awayTeam.isPresent()) {
            Match match = new Match(homeTeam.get(), awayTeam.get(), dateTime);
            matchConsumer.accept(match);
            parsed++;
          } else {
            System.err.println("Could not find team " + homeTeamString + " or " + awayTeamString);
          }
        } catch (IllegalStateException e) {
          System.err.println(row);
          error++;
        }
      }
    });

    System.out.println("parsed: " + parsed + "; error:" + error);
  }

  private boolean isDateHeader(Cell cell) {
    if (cell == null) return false;

    CellStyle style = cell.getCellStyle();
    Color backgroundColor = style.getFillBackgroundColorColor();

    return backgroundColor != null;
  }
}
