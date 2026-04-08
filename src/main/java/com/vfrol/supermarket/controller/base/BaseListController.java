package com.vfrol.supermarket.controller.base;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public abstract class BaseListController<T> {

    @Inject
    protected ViewManager viewManager;

    @Inject
    protected SessionManager sessionManager;

    protected abstract TableView<T> getTableView();

    protected abstract void showDetails(T item);

    protected void setupTableDoubleClick() {
        getTableView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                T selected = getTableView().getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showDetails(selected);
                }
            }
        });
    }

    protected void toggleFilterPanel(VBox filterPanel) {
        boolean isCurrentlyVisible = filterPanel.isVisible();
        filterPanel.setVisible(!isCurrentlyVisible);
        filterPanel.setManaged(!isCurrentlyVisible);
    }
}