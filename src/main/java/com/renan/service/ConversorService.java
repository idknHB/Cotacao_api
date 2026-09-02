package com.renan.service;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;

public class ConversorService {

    private final ApiCliente api =
            new ApiCliente();

    private final Gson gson =
            new Gson();

    public double converter(
            String origem,
            String destino,
            double valor
    ) throws Exception {

        String json =
                api.buscarCotacao(origem);

        CurrencyResponse response =
                gson.fromJson(
                        json,
                        CurrencyResponse.class
                );

        double taxa =
                response.getRates()
                        .get(destino);

        return valor * taxa;
    }
}