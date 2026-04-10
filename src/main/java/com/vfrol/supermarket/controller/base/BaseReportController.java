package com.vfrol.supermarket.controller.base;

import com.vfrol.supermarket.controller.util.AlertHelper;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public abstract class BaseReportController<T> extends BaseModalController {

    @FXML protected TableView<T> previewTable;
    @FXML protected Button exportButton;

    protected List<T> reportData;

    protected abstract BaseExcelExporter<T> getExporter();
    protected abstract String getDefaultFileName();
    protected abstract String getReportTitle();

    public void setData(List<T> data) {
        this.reportData = data;
        previewTable.setItems(FXCollections.observableArrayList(data));
    }

    @FXML
    protected void onExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getReportTitle());
        fileChooser.setInitialFileName(getDefaultFileName());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());
        if (file == null) {
            return;
        }

        exportButton.setDisable(true);

        AsyncRunner.runAsync(
                () -> {
                    getExporter().export(reportData, file);
                    return file.getAbsolutePath();
                },
                (filePath) -> {
                    AlertHelper.showInfo("Export Successful", "File saved to:\n" + filePath);
                    closeWindow(exportButton);
                },
                previewTable
        );
    }

    @FXML
    protected void onCancel() {
        closeWindow(exportButton);
    }
}