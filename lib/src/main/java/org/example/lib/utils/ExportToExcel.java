package org.example.lib.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportToExcel {

    public static <T> void export(TableView<T> table, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn nơi lưu file Excel");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sheet 1");
            XSSFRow headerRow = sheet.createRow(0);

            List<TableColumn<T, ?>> columns = table.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                headerRow.createCell(i).setCellValue(columns.get(i).getText());
            }

            ObservableList<T> items = table.getItems();
            for (int row = 0; row < items.size(); row++) {
                Row sheetRow = sheet.createRow(row + 1);
                T item = items.get(row);
                for (int col = 0; col < columns.size(); col++) {
                    TableColumn<T, ?> column = columns.get(col);
                    Cell cell = sheetRow.createCell(col);
                    Object value = column.getCellData(item);
                    if (value != null) {
                        switch (value.getClass().getSimpleName()) {
                            case "String":
                                cell.setCellValue((String) value);
                                break;
                            case "Double":
                                cell.setCellValue((Double) value);
                                break;
                            case "Integer":
                                cell.setCellValue((Integer) value);
                                break;
                            case "Boolean":
                                cell.setCellValue((Boolean) value);
                                break;
                            default:
                                cell.setCellValue(value.toString());
                                break;
                        }
                    }
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
