package nl.woodmanict.koos.parsers;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.function.Predicate;
import java.util.function.Consumer;

import org.apache.poi.openxml4j.opc.OPCPackage;
import static org.apache.poi.openxml4j.opc.PackageAccess.READ;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public abstract class AbstractExcelParser {
  private Sheet mainSheet;

  public AbstractExcelParser(String fileName) throws Exception {
    OPCPackage opcPackage = OPCPackage.open(fileName, READ);
    Workbook workbook = WorkbookFactory.create(opcPackage);
    mainSheet = workbook.getSheetAt(0);
  }

  protected void parseRows(int from, int to, Predicate<Row> rowPredicate, Consumer<Row> rowConsumer) {
    Iterator<Row> rows = mainSheet.rowIterator();

    while(rows.hasNext()) {
      Row row = rows.next();
      int rowNumber = row.getRowNum();

      if (rowNumber > to) break;
      if (rowNumber < from || (rowPredicate != null && !rowPredicate.test(row))) continue;

      rowConsumer.accept(row);
    }
  }

  protected Sheet getMainSheet(){
    return mainSheet;
  }
}
