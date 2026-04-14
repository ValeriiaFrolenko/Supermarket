package com.vfrol.supermarket;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.database.DatabaseInitializer;
import com.vfrol.supermarket.config.SupermarketModule;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SupermarketApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Injector injector = Guice.createInjector(new SupermarketModule());
        injector.getInstance(DatabaseInitializer.class).initialize();

        seedInitialAdminIfEmpty(injector);

        ViewManager viewManager = injector.getInstance(ViewManager.class);
        Scene scene = new Scene(viewManager.initialize(), 900, 600);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.setTitle("Supermarket");
        stage.setScene(scene);
        stage.show();

        viewManager.navigateTo(AppView.LOGIN);
    }

    private void seedInitialAdminIfEmpty(Injector injector) {
        EmployeeService employeeService = injector.getInstance(EmployeeService.class);
        if (employeeService.isEmpty()) {
            employeeService.createInitialAdmin();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--setup-db")) {
            DevDataSeeder.main(new String[]{});
            return;
        }
        launch(args);
    }
}