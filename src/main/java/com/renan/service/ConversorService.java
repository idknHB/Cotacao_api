package com.renan.service;

import com.google.gson.Gson;
import com.renan.model.CurrencyResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ConversorService {

    private final ApiCliente api =
            new ApiCliente();

    private final Gson gson =
            new Gson();

    private CurrencyResponse responseAtual;

    private final Map<String, CurrencyResponse> cache =
            new HashMap<>();

    public CurrencyResponse atualizarTaxa(String origem) throws IOException, InterruptedException{
        if(cache.containsKey(origem)){
            return cache.get(origem);
        }
        String json = api.buscarCotacao(origem);

        CurrencyResponse response =
                gson.fromJson(
                        json,
                        CurrencyResponse.class
                );

        cache.put(origem, response);

        return response;
    }

    public double converter(String origem, String destino, double valor) throws Exception {

        CurrencyResponse response = atualizarTaxa(origem);

        double taxa =
                response.getRates()
                        .get(destino);

        return valor * taxa;
    }
}