package nl.woodmanict.koos.parsers;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import nl.woodmanict.koos.model.Team;

import org.apache.poi.openxml4j.opc.OPCPackage;
import static org.apache.poi.openxml4j.opc.PackageAccess.READ;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellStyle;

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
