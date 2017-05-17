package nl.choutman.allinn.koos.parsers;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.util.Iterator;
import java.util.function.Consumer;

public abstract class AbstractExcelParser {
  private Sheet mainSheet;

  public AbstractExcelParser(String fileName) throws Exception {
    OPCPackage opcPackage = OPCPackage.open(fileName, PackageAccess.READ);
    Workbook workbook = WorkbookFactory.create(opcPackage);
    mainSheet = workbook.getSheetAt(0);
  }

  protected void parseRows(int from, int to, Consumer<Row> rowConsumer) {
    Iterator<Row> rows = mainSheet.rowIterator();

    while(rows.hasNext()) {
      Row row = rows.next();
      int rowNumber = row.getRowNum();

      if (rowNumber > to - 1) break;
      if (rowNumber < from - 1) continue;

      rowConsumer.accept(row);
    }
  }
}
