package com.renan;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;
import com.renan.serivce.ApiCliente;
import com.renan.model.Moeda;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

import java.util.Currency;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        Label titulo = new Label("Cotação de Moedas");
        Label resultado = new Label("Nenhuma consulta realizada");
        Label lblOrigem = new Label("Moeda de origem");
        Label lblDestino = new Label("Moeda de destino");
        Label lblValor = new Label("Valor");
        Button btnConverter = new Button("Converter");
        Button btnTrocar = new Button("⇅");
        TextField txtValor = new TextField();

        ComboBox<Moeda> moedaOrigem =
                new ComboBox<>();

        ComboBox<Moeda> moedaDestino =
                new ComboBox<>();

        ApiCliente api = new ApiCliente();

        String json =
                api.buscarCotacao("USD");

        Gson gson = new Gson();

        CurrencyResponse response =
                gson.fromJson(
                        json,
                        CurrencyResponse.class);

       for(String codigo : response.getRates().keySet()){

           String nome;

           try{
               nome = Currency
                       .getInstance(codigo)
                       .getDisplayName();
           }catch (Exception e){
               nome = codigo;
           }
           moedaOrigem.getItems().add(
                   new Moeda(
                           codigo,
                           nome
                   )
           );
           moedaDestino.getItems().add(
                   new Moeda(
                           codigo,
                           nome
                   )
           );
       }

        btnConverter.setOnAction(event -> {
            try {
                String origem =
                        moedaOrigem.getValue().getCodigo();

                String destino =
                        moedaDestino.getValue().getCodigo();

                String jsonAtual =
                        api.buscarCotacao(origem);

                CurrencyResponse responseAtual =
                        gson.fromJson(
                        jsonAtual,
                        CurrencyResponse.class);

                double valor = Double.parseDouble(txtValor.getText());
                double taxa = responseAtual.getRates().get(destino);
                double convertido = valor * taxa;

                resultado.setText(
                        String.format(
                                "%.2f %s - %.2f %s",
                                valor,
                                origem,
                                convertido,
                                destino
                        )
                );

            }catch (Exception ex){
                resultado.setText(ex.getMessage());
                ex.printStackTrace();
            }
        });

       btnTrocar.setOnAction(event -> {
           Moeda temp = moedaOrigem.getValue();

           moedaOrigem.setValue(
                   moedaDestino.getValue());

           moedaDestino.setValue(temp);
       });


        VBox layout = new VBox(20);
        VBox card = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1e1e;");
        layout.setAlignment(Pos.CENTER);
        txtValor.setPromptText("Digite o valor");

        moedaOrigem.setPrefWidth(300);
        moedaDestino.setPrefWidth(300);
        txtValor.setPrefWidth(300);
        btnConverter.setPrefWidth(300);

        lblOrigem.setStyle("-fx-text-fill: white;");
        lblDestino.setStyle("-fx-text-fill: white;");
        lblValor.setStyle("-fx-text-fill: white;");

        layout.getChildren().addAll(
                titulo,
                moedaOrigem,
                btnTrocar,
                moedaDestino,
                txtValor,
                btnConverter,
                resultado
        );
        card.setStyle(
                "-fx-background-color: #252526;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 25;"
        );
        btnConverter.setStyle(
                "-fx-background-color: #0078D7;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"

        );
        titulo.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-text-fill: white;"+
                "-fx-font-weight: bold;"
        );
        resultado.setStyle(
                "-fx-text-fill: #4CAF50;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
                );
        Scene scene = new Scene(layout, 500, 350);

        stage.setTitle("Cotação API");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}