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

    private boolean trocando = false;
    private boolean atualizando = false;

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
                        converterOrigemParaDestino();
                    });

                    new Thread(task).start();

                });


        moedaDestino.setOnAction(
                event -> converterOrigemParaDestino()
        );

        txtOrigem.textProperty().addListener(
                (obs, oldValue, newValue) -> {
                    if(atualizando){return;}
                    converterOrigemParaDestino();
                });
        txtDestino.textProperty().addListener((obs, oldValue, newValue) -> {
            if(atualizando){return;}

            converterDestinoParaOrigem();
        });

        btnTrocar.setOnAction(event -> {

            trocando  = true;

            String txtTemp = txtOrigem.getText();

            txtOrigem.setText(txtDestino.getText());

            txtDestino.setText(txtTemp);

            Moeda moedaTemp =
                    moedaOrigem.getValue();

            moedaOrigem.setValue(
                    moedaDestino.getValue()
            );

            moedaDestino.setValue(moedaTemp);

            trocando = false;

            converterOrigemParaDestino();
        });
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

    private void converterOrigemParaDestino() {
        if(trocando){return;}
        try {
            if(txtOrigem.getText().isBlank()){
                txtDestino.clear();
                return;
            }
            atualizando = true;

            double valor =
                    Double.parseDouble(
                            txtOrigem.getText()
                                    .replace(",", ".")
                    );

            double convertido =
                    conversor.converter(
                            moedaOrigem.getValue().getCodigo(),
                            moedaDestino.getValue().getCodigo(),
                            valor
                    );

            txtDestino.setText(
                    String.format(
                            "%.4f",
                            convertido
                    )
            );


        }catch (Exception ignore){
        }
        finally {
            atualizando = false;
        }
    }

    private void converterDestinoParaOrigem() {
        if(trocando){return;}
        try {
            if(txtDestino.getText().isBlank()){
                txtOrigem.clear();
                return;
            }
            atualizando = true;

            double valor =
                    Double.parseDouble(
                            txtDestino.getText()
                                    .replace(",", ".")
                    );

            double convertido =
                    conversor.converter(
                            moedaDestino.getValue().getCodigo(),
                            moedaOrigem.getValue().getCodigo(),
                            valor
                    );

            txtOrigem.setText(
                    String.format(
                            "%.4f",
                            convertido
                    )
            );


        }catch (Exception ignore){
        }
        finally {
            atualizando = false;
        }
    }
}