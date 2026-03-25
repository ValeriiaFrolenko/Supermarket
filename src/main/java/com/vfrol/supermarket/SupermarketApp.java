package com.vfrol.supermarket;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vfrol.supermarket.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SupermarketApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Injector injector = Guice.createInjector(new SupermarketModule());
        DatabaseInitializer initializer = injector.getInstance(DatabaseInitializer.class);
        initializer.initialize();
        FXMLLoader fxmlLoader = new FXMLLoader(SupermarketApp.class.getResource("view/main-view.fxml"));
        fxmlLoader.setControllerFactory(injector::getInstance);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Supermarket");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}