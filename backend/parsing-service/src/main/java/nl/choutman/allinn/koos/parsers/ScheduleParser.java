package nl.choutman.allinn.koos.parsers;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
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

public class ScheduleParser extends AbstractExcelParser {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleParser.class);

    private final TeamDao teamDao;
    private LocalDateTime dateTime = null;

    private int parsed = 0;
    private int error = 0;

    public ScheduleParser(String path) throws Exception {
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

                logger.debug("Set time to: {0}", dateTime);
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
                        logger.warn("Could not find team {0} or {1}", homeTeamString, awayTeamString);
                    }
                } catch (IllegalStateException e) {
                    logger.warn("Unable to parse row");
                    logger.debug(row);

                    error++;
                }
            }
        });

        logger.debug("Rows parsed: {0}; error: {1}", parsed, error);
    }

    private boolean isDateHeader(Cell cell) {
        if (cell == null) return false;

        CellStyle style = cell.getCellStyle();
        Color backgroundColor = style.getFillBackgroundColorColor();

        return backgroundColor != null;
    }
}
