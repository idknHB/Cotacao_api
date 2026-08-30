package com.renan.model;

public class Moeda {
    private String codigo;
    private  String nome;

    public Moeda(String codigo, String nome){
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo(){
        return codigo;
    }
    public String getNome(){
        return nome;
    }

    @Override
    public String toString(){
     return codigo + " - " + nome;
    }
}
