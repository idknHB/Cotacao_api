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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Currency;

public class MainView {

    private final VBox layout =
            new VBox(20);

    private final VBox card =
            new VBox(15);

    private final Label titulo =
            new Label("Cotação de Moedas");

    private final TextField txtOrigem =
            new TextField();
    private final TextField txtDestino =
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

        aplicarEstilos();

        configurarEventos();

        montarLayout();
    }

    public void aplicarEstilos(){
        layout.setStyle(
                Styles.ROOT
        );

        titulo.setStyle(
                Styles.TITLE
        );

        moedaOrigem.setStyle(
                Styles.COMBO
        );

        moedaDestino.setStyle(
                Styles.COMBO
        );

        btnTrocar.setStyle(
                Styles.SWAP_BUTTON
        );

        txtDestino.setEditable(false);

        moedaOrigem.setPrefWidth(300);

        moedaDestino.setPrefWidth(300);

        btnTrocar.setMinSize(50, 50);

        btnTrocar.setMaxSize(50, 50);
    }

    public VBox getLayout() {
        return layout;
    }

    private void montarLayout() {

        HBox linhaOrigem = new HBox(10);

        linhaOrigem.setAlignment(Pos.CENTER);

        linhaOrigem.getChildren().addAll(
                txtOrigem,
                moedaOrigem
        );

        HBox linhaDestino = new HBox(10);

        linhaDestino.setAlignment(Pos.CENTER);

        linhaDestino.getChildren().addAll(
                txtDestino,
                moedaDestino
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        card.setAlignment(Pos.CENTER);

        card.setStyle(
                Styles.CARD
        );

        card.getChildren().addAll(
                titulo,
                linhaOrigem,
                btnTrocar,
                linhaDestino
        );

        layout.getChildren().add(card);
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

        txtOrigem.textProperty().addListener(
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
                            || txtOrigem.getText().isEmpty()
            ) {
                return;
            }

            double valor =
                    Double.parseDouble(
                            txtOrigem.getText()
                    );

            double convertido =
                    conversor.converter(
                            moedaOrigem.getValue().getCodigo(),
                            moedaDestino.getValue().getCodigo(),
                            valor
                    );

            txtDestino.setText(
                    String.format(
                            "%.2f",
                            convertido
                    )
            );

        } catch (Exception e) {
            txtDestino.setText("Erro ao converter");
            e.printStackTrace();
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