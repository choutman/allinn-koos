package nl.choutman.allinn.koos.parsers;

import nl.choutman.allinn.koos.dao.TeamDao;
import nl.choutman.allinn.koos.dao.TeamDaoImpl;
import nl.choutman.allinn.koos.model.Position;
import nl.choutman.allinn.koos.model.Team;

import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.util.CellReference.convertColStringToIndex;

/**
 * Created by choutman on 16/05/2017.
 */
public class StandingsParser extends AbstractExcelParser {
    private static final int TEAM_COLUMN = convertColStringToIndex("L");
    private static final int POINTS_COLUMN = convertColStringToIndex("M");
    private static final int GAMES_PLAYED_COLUMN = convertColStringToIndex("N");

    private final TeamDao teamDao;

    public StandingsParser(String fileName) throws Exception {
        super(fileName);

        teamDao = TeamDaoImpl.getInstance();
    }

    public List<Position> parseStandings() {
        List<Position> standings = new ArrayList<>();
        parseRows(3, 21, row -> {
            final String teamName = row.getCell(TEAM_COLUMN).getStringCellValue();

            final Team team = teamDao.findTeam(teamName).get();
            final int points = (int) row.getCell(POINTS_COLUMN).getNumericCellValue();
            final int gamesPlayed = (int) row.getCell(GAMES_PLAYED_COLUMN).getNumericCellValue();

            final Position position = new Position(team.getName(), points, gamesPlayed);
            standings.add(position);
        });

        return standings;
    }
}
