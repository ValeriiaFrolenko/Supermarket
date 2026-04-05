package com.vfrol.supermarket.controller;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class BaseModalController {

    @Inject
    protected ViewManager viewManager;

    @Inject
    protected SessionManager sessionManager;

    protected void closeWindow(Node node) {
        Stage window = (Stage) node.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}