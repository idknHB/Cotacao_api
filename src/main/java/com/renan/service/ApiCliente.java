package com.renan.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ApiCliente {

    public String buscarCotacao(String moeda) throws IOException, InterruptedException {
        //Criar o cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        //Monta a requisição
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://open.er-api.com/v6/latest/" + moeda))
                .GET()
                .build();

        long inicio = System.currentTimeMillis();

        //Envia a requisição e recebe a resposta
        HttpResponse<String> response =
                client.send(request,
                        HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
