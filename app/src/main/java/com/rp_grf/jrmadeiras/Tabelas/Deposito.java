package com.rp_grf.jrmadeiras.Tabelas;

public class Deposito implements Comparable<Deposito>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_dep;
    private String nom_dep;

    public Deposito() {
    }

    public Deposito(Long cod_dep, String nom_dep) {
        this.cod_dep = cod_dep;
        this.nom_dep = nom_dep;
    }

    public Long getCod_dep() {
        return cod_dep;
    }

    public void setCod_dep(Long cod_dep) {
        this.cod_dep = cod_dep;
    }

    public String getNom_dep() {
        return nom_dep;
    }

    public void setNom_dep(String nom_dep) {
        this.nom_dep = nom_dep;
    }

    //Método de comparação do número de sequência do programa
    @Override
    public int compareTo(Deposito codigo) {
        return this.cod_dep.compareTo(codigo.cod_dep);
    }
}
