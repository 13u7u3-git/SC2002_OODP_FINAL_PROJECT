package helper;

import java.util.List;

import static helper.ColumnFormatter.formatColumn;

public class TablePrinter {
   public void PrintTableRow(Integer COLUMN_WIDTH, List<String> rowData) {
      StringBuilder row = new StringBuilder("| ");
      for (String str : rowData) {
         row.append(formatColumn(str, COLUMN_WIDTH)).append(" | ");
      }
      Color.println(row.toString(), Color.YELLOW);
   }
}
