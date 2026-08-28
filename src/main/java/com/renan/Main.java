package com.renan;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;
import com.renan.serivce.ApiCliente;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Label titulo = new Label("Cotação de Moedas");

        ComboBox<String> moedas = new ComboBox<>();
        moedas.getItems().addAll(
                "USD",
                "EUR",
                "GBP",
                "JPY"
        );
        moedas.setValue("USD");

        Label resultado = new Label("Nenhuma consulta realizada");

        Button btnBuscar = new Button("Buscar Cotação");

        btnBuscar.setOnAction(e -> {
            try {
                String moedaSelecionada =
                        moedas.getValue();

                ApiCliente api = new ApiCliente();

                String json =
                        api.buscarCotacao(moedaSelecionada);

                Gson gson = new Gson();

                CurrencyResponse response =
                        gson.fromJson(
                                json,
                                CurrencyResponse.class);

                resultado.setText(
                        "BRL: " +
                        response.getRates().get("BRL")
                );
            }catch (Exception ex){
                resultado.setText("Erro ao consultar API");
                ex.printStackTrace();
            }
        });


        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1e1e;");
        layout.getChildren().addAll(
                titulo,
                moedas,
                btnBuscar,
                resultado
        );
        titulo.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-text-fill: white;"+
                "-fx-font-weight: bold;"
        );
        resultado.setStyle("-fx-text-fill:white");
        Scene scene = new Scene(layout, 500, 350);

        stage.setTitle("Cotação API");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}