package com.renan;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;
import com.renan.serivce.ApiCliente;
import com.renan.model.Moeda;
import com.renan.ui.Styles;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

import java.util.Currency;

public class Main extends Application {

    private ComboBox<Moeda> moedaOrigem;
    private ComboBox<Moeda> moedaDestino;
    private TextField txtValor;
    private Label resultado;
    private void converter(){
        try{
            Moeda origem = moedaOrigem.getValue();
            Moeda destino = moedaDestino.getValue();

            if(origem == null || destino == null){
                return;
            }

            if(txtValor.getText().isEmpty()){
                return;
            }

            double valor =
                    Double.parseDouble(txtValor.getText());

            ApiCliente api = new ApiCliente();

            String json =
                    api.buscarCotacao(
                            origem.getCodigo()
                    );

            Gson gson = new Gson();

            CurrencyResponse response =
                    gson.fromJson(
                            json,
                            CurrencyResponse.class
                    );

            double taxa =
                    response.getRates()
                            .get(destino.getCodigo());

            double convertido =
                    valor * taxa;

            resultado.setText(
                    String.format(
                            "$%.2f",
                            convertido
                    )
            );

        }catch(Exception e){
            resultado.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        Label titulo = new Label("Cotação de Moedas");
        resultado = new Label("Nenhuma consulta realizada");
        Label lblOrigem = new Label("Moeda de origem");
        Label lblDestino = new Label("Moeda de destino");
        Label lblValor = new Label("Valor");
        Button btnTrocar = new Button("\uD83D\uDD04");
        txtValor = new TextField();

        moedaOrigem =
                new ComboBox<>();

        moedaDestino =
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

       moedaOrigem.setOnAction(event ->
               converter()
       );
       moedaDestino.setOnAction(event ->
               converter()
       );

        txtValor.textProperty().addListener(
                (obs, oldValue, newValue) ->
                        converter()
        );

       btnTrocar.setOnAction(event -> {
           Moeda temp = moedaOrigem.getValue();

           moedaOrigem.setValue(
                   moedaDestino.getValue());

           moedaDestino.setValue(temp);

           converter();
       });


        VBox layout = new VBox(20);
        VBox card = new VBox(15);
        VBox cardResultado = new VBox(resultado);
        DropShadow shadow = new DropShadow();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #121212;");
        layout.setAlignment(Pos.CENTER);
        txtValor.setPromptText("Digite o valor");

        moedaOrigem.setPrefWidth(300);
        moedaDestino.setPrefWidth(300);
        txtValor.setPrefWidth(300);

        lblOrigem.setStyle("-fx-text-fill: white;");
        lblDestino.setStyle("-fx-text-fill: white;");
        lblValor.setStyle("-fx-text-fill: white;");

        titulo.setStyle(Styles.TITLE);
        resultado.setStyle(Styles.RESULT);

        card.setStyle(Styles.CARD);

        moedaOrigem.setPrefHeight(35);
        moedaDestino.setPrefHeight(35);
        txtValor.prefHeight(35);

        shadow.setRadius(15);

        cardResultado.setStyle(
                "-fx-background-color: #1A1A1A;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 20;"
        );

        cardResultado.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                titulo,
                moedaOrigem,
                btnTrocar,
                moedaDestino,
                txtValor,
                cardResultado
        );
        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Cotação API");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}