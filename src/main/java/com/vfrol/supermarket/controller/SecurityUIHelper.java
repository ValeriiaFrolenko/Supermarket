package com.vfrol.supermarket.controller;

import com.vfrol.supermarket.config.SessionManager;
import javafx.scene.Node;

public final class SecurityUIHelper {

    private SecurityUIHelper() {}

    public static void configureManagerOnlyNodes(SessionManager sessionManager, Node... nodes) {
        boolean isManager = sessionManager.isManager();
        for (Node node : nodes) {
            if (node != null) {
                node.setVisible(isManager);
                node.setManaged(isManager);
            }
        }
    }
}