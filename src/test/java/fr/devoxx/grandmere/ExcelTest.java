package fr.devoxx.grandmere;

import com.google.common.io.Resources;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
                assertThat(row.getCell(column)).isNotNull();
            }
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
