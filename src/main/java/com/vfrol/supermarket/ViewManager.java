package com.vfrol.supermarket;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
public class ViewManager {

    private final Injector injector;
    @Setter
    private StackPane contentArea;

    private final Map<AppView, Parent> cache = new HashMap<>();

    @Inject
    public ViewManager(Injector injector) {
        this.injector = injector;
    }

    public void navigateTo(AppView view) {
        if (contentArea == null) {
            throw new IllegalStateException("ContentArea not set! Call setContentArea() first.");
        }

        Parent root = cache.computeIfAbsent(view, this::loadView);
        contentArea.getChildren().setAll(root);
    }

    private Parent loadView(AppView view) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SupermarketApp.class.getResource(view.getFxmlPath())
            );
            loader.setControllerFactory(injector::getInstance);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + view.getFxmlPath(), e);
        }
    }


    public void showDialog(AppView view) {
        showDialog(view, null);
    }


    public <T> T showDialog(AppView view, Consumer<T> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SupermarketApp.class.getResource(view.getFxmlPath())
            );

            loader.setControllerFactory(injector::getInstance);

            Parent root = loader.load();
            T controller = loader.getController();

            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(view.name().replace("_", " "));
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            if (contentArea != null && contentArea.getScene() != null) {
                dialogStage.initOwner(contentArea.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            return controller;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dialog: " + view.getFxmlPath(), e);
        }
    }
}