package com.vfrol.supermarket.controller.util;

import com.vfrol.supermarket.config.SessionManager;
import javafx.scene.Node;

public final class SessionUIHelper {

    private SessionUIHelper() {}

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