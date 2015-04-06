package fr.devoxx.grandmere;

import com.google.common.io.Resources;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Throwables.propagate;
import static org.assertj.core.api.Assertions.assertThat;

public class ExcelTest {
    @Test
    public void should_load_excel() {
        Workbook workbook = getWorkbook("perf.xlsx");
        Sheet perf = workbook.getSheetAt(0);
        for (int rowId = perf.getFirstRowNum(); rowId < perf.getLastRowNum(); rowId++) {
            Row row = perf.getRow(rowId);
            for (int column = 0; column < row.getLastCellNum(); column++) {
                Cell cell = row.getCell(column);
                System.out.println(cell);
                assertThat(cell).isNotNull();
            }
        }
    }

    @Test
    public void should_load_execute() {
        Workbook workbook = getWorkbook("perf.xlsx");
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        Sheet perf = workbook.getSheetAt(0);
        for (int rowId = perf.getFirstRowNum(); rowId < perf.getLastRowNum(); rowId++) {
            Row row = perf.getRow(rowId);
            for (int column = 0; column < row.getLastCellNum(); column++) {
                String cell = cell(row.getCell(column), evaluator);
                System.out.println(cell);
                assertThat(cell).describedAs("Cell " + rowId + ":" + column).isNotNull();
            }
        }
    }

    private String cell(Cell cell, FormulaEvaluator evaluator) {
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
                return evaluator.evaluate(cell).formatAsString();
            case Cell.CELL_TYPE_NUMERIC:
                return "" + cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return "" + cell.getBooleanCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_ERROR:
                return null;
            default:
                throw new IllegalStateException("Could not handle " + cell.getCellType());
        }
    }

    public static Workbook getWorkbook(String path) {
        try (InputStream input = openStream(path)) {
            if (!path.endsWith(".xls")) {
                return new XSSFWorkbook(input);
            } else {
                return new HSSFWorkbook(input);
            }
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private static InputStream openStream(String path) throws IOException {
        try {
            return Resources.getResource(path).openStream();
        } catch (IllegalArgumentException e) {
            return new FileInputStream(path);
        }
    }

}
