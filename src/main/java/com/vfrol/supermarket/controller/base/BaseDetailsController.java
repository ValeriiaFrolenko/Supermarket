package com.vfrol.supermarket.controller.base;

import com.vfrol.supermarket.controller.util.AlertHelper;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public abstract class BaseDetailsController<D> extends BaseModalController {

    @FXML protected VBox detailsPanel;

    protected D currentItem;
    protected abstract String getEntityName();
    protected abstract void populateFields(D dto);
    protected abstract void deleteEntity();
    protected abstract void navigateToForm();


    public void setDetails(D dto) {
        this.currentItem = dto;
        populateFields(dto);
    }

    @FXML
    public void onEdit() {
        navigateToForm();
        hide();
    }

    @FXML
    public void onDelete() {
        if (AlertHelper.confirmDelete(getEntityName())) {
            AsyncRunner.runAsync(
                    this::deleteEntity,
                    this::hide,
                    detailsPanel
            );
        }
    }

    @FXML
    public void hide() {
        closeWindow(detailsPanel);
    }
}