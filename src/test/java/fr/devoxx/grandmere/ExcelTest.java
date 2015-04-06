package fr.devoxx.grandmere;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.google.common.io.Resources;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Throwables.propagate;
import static org.assertj.core.api.Assertions.assertThat;

public class ExcelTest extends AbstractBenchmark {
    private static Workbook workbook;
    private static CreationHelper creationHelper;

    @BeforeClass
    public static void loadWorkBook() {
        workbook = getWorkbook("perf.xlsx");
        creationHelper = workbook.getCreationHelper();
    }

    @BenchmarkOptions(benchmarkRounds = 1000, warmupRounds = 100, concurrency = 8)
    @Test
    public void should_load_excel() {
        Sheet perf = workbook.getSheetAt(0);
        List<String> formulas = new ArrayList<>();
        for (int rowId = perf.getFirstRowNum(); rowId < perf.getLastRowNum(); rowId++) {
            Row row = perf.getRow(rowId);
            for (int column = 0; column < row.getLastCellNum(); column++) {
                String formula = row.getCell(column).toString();
                assertThat(formula).isNotNull();
                formulas.add(formula);
            }
        }
        assertThat(formulas).hasSize(189);
    }

    @BenchmarkOptions(benchmarkRounds = 1000, warmupRounds = 100, concurrency = 8)
    @Test
    public void should_load_execute() {
        FormulaEvaluator evaluator = creationHelper.createFormulaEvaluator();

        Sheet perf = workbook.getSheetAt(0);
        for (int rowId = perf.getFirstRowNum(); rowId < perf.getLastRowNum(); rowId++) {
            Row row = perf.getRow(rowId);
            for (int column = 0; column < row.getLastCellNum(); column++) {
                assertThat(cell(row.getCell(column), evaluator)).isNotNull();
            }
        }
    }

    private String cell(Cell cell, FormulaEvaluator evaluator) {
        switch (cell.getCellType()) {
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
