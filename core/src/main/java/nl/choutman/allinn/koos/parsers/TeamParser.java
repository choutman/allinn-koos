package nl.choutman.allinn.koos.parsers;

import nl.choutman.allinn.koos.model.Team;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Consumer;

public class TeamParser extends AbstractExcelParser {
  public TeamParser(String path) throws Exception {
    super(path);
  }

  public void parseTeams(Consumer<Team> teamConsumer) {

    parseRows(3, 21, row -> {
      Cell teamCell = row.getCell(0);
      String teamName = teamCell.getStringCellValue();

      Cell playingDoublesCell = row.getCell(1);
      boolean isPlayingDoubles = playingDoublesCell != null;

      Team team = new Team(teamName, isPlayingDoubles);
      teamConsumer.accept(team);
    });
  }
}
