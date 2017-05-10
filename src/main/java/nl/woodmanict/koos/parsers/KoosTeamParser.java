package nl.woodmanict.koos.parsers;

import nl.woodmanict.koos.model.Team;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Consumer;

public class KoosTeamParser extends AbstractExcelParser {
  public KoosTeamParser(String path) throws Exception {
    super(path);
  }

  public void parseTeams(Consumer<Team> teamConsumer) {

    parseRows(2, 20, null, row -> {
      Cell teamCell = row.getCell(0);
      String teamName = teamCell.getStringCellValue();

      Cell playingDoublesCell = row.getCell(1);
      boolean isPlayingDoubles = playingDoublesCell != null;

      Team team = new Team(teamName, isPlayingDoubles);
      teamConsumer.accept(team);
    });
  }
}
