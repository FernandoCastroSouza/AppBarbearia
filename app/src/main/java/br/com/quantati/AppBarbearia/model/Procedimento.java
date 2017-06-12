package br.com.quantati.AppBarbearia.model;

/**
 * Created by Fernando on 12/06/2017.
 */

public enum Procedimento {

    BA("Barba"),
    CA("Cabelo"),
    BI("Bigode"),
    TO("Todos");

    private String procedimento;

    Procedimento(String procedimento){
        this.procedimento = procedimento;
    }

    @Override
    public String toString() {
        return procedimento;
    }
}
