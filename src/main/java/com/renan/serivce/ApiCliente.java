package com.renan.serivce;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;

public class ApiCliente {

    public String buscarCotacao(String moeda) throws IOException, InterruptedException {
        //Criar o cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        //Monta a requisição
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://open.er-api.com/v6/latest/USD" + moeda))
                .GET()
                .build();

        //Envia a requisição e recebe a resposta
        HttpResponse<String> response =
                client.send(request,
                        HttpResponse.BodyHandlers.ofString());

        //Teste de resposta
        //Codigo esperado : 200
        System.out.println(response.statusCode());
        return response.body();
    }
}
