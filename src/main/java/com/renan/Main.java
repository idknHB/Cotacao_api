package com.renan;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;
import com.renan.serivce.ApiCliente;
import com.renan.model.Moeda;
import javafx.application.Application;
import javafx.geometry.Insets;
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
        Button btnConverter = new Button("Converter");
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


        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1e1e;");
        txtValor.setPromptText("Digite o valor");
        layout.getChildren().addAll(
                titulo,
                moedaOrigem,
                moedaDestino,
                txtValor,
                btnConverter,
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