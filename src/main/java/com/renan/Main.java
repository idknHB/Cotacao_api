package com.renan;

import com.renan.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        MainView view = new MainView();

        Scene scene = new Scene(
                view.getLayout(),
                800,
                600
        );

        stage.setTitle("Cotação API");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}