package com.renan.model;

import java.util.Objects;

public class Moeda {

    private String codigo;
    private String nome;

    public Moeda(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Moeda moeda = (Moeda) obj;

        return codigo.equals(moeda.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}
