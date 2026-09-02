package com.renan.ui;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;
import com.renan.model.Moeda;
import com.renan.service.ApiCliente;
import com.renan.service.ConversorService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Currency;

public class MainView {

    private final VBox layout =
            new VBox(20);

    private final Label titulo =
            new Label("Cotação de Moedas");

    private final Label resultado =
            new Label("Nenhuma consulta realizada");

    private final TextField txtValor =
            new TextField();

    private final ComboBox<Moeda> moedaOrigem =
            new ComboBox<>();

    private final ComboBox<Moeda> moedaDestino =
            new ComboBox<>();

    private final Button btnTrocar =
            new Button("🔄");

    private final ConversorService conversor =
            new ConversorService();

    public MainView() {

        carregarMoedas();

        configurarEventos();

        montarLayout();
    }

    public VBox getLayout() {
        return layout;
    }

    private void montarLayout() {

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(
                titulo,
                moedaOrigem,
                btnTrocar,
                moedaDestino,
                txtValor,
                resultado
        );
    }

    private void configurarEventos() {

        moedaOrigem.setOnAction(
                event -> {

                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            conversor.atualizarTaxa(
                                    moedaOrigem.getValue().getCodigo()
                            );
                            return null;
                        }

                    };
                    task.setOnSucceeded(e -> {
                        converter();
                    });

                    new Thread(task).start();

                });


        moedaDestino.setOnAction(
                event -> converter()
        );

        txtValor.textProperty().addListener(
                (obs, oldValue, newValue) ->
                        converter()
        );

        btnTrocar.setOnAction(event -> {

            Moeda temp =
                    moedaOrigem.getValue();

            moedaOrigem.setValue(
                    moedaDestino.getValue()
            );

            moedaDestino.setValue(temp);

            converter();
        });
    }

    private void converter() {
        
        try {

            if (
                    moedaOrigem.getValue() == null
                            || moedaDestino.getValue() == null
                            || txtValor.getText().isEmpty()
            ) {
                return;
            }

            double valor =
                    Double.parseDouble(
                            txtValor.getText()
                    );

            double convertido =
                    conversor.converter(
                            moedaOrigem.getValue().getCodigo(),
                            moedaDestino.getValue().getCodigo(),
                            valor
                    );

            resultado.setText(
                    String.format(
                            "%.2f",
                            convertido
                    )
            );

        } catch (Exception e) {
            resultado.setText("Erro ao converter");
        }
    }

    private void carregarMoedas() {

        try {

            ApiCliente api =
                    new ApiCliente();

            String json =
                    api.buscarCotacao("USD");

            Gson gson =
                    new Gson();

            CurrencyResponse response =
                    gson.fromJson(
                            json,
                            CurrencyResponse.class
                    );

            for (
                    String codigo :
                    response.getRates().keySet()
            ) {

                String nome;

                try {

                    nome = Currency
                            .getInstance(codigo)
                            .getDisplayName();

                } catch (Exception e) {

                    nome = codigo;
                }

                Moeda moeda =
                        new Moeda(
                                codigo,
                                nome
                        );

                moedaOrigem
                        .getItems()
                        .add(moeda);

                moedaDestino
                        .getItems()
                        .add(
                                new Moeda(
                                        codigo,
                                        nome
                                )
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}