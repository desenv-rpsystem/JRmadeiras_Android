package com.rp_grf.jrmadeiras.Tabelas;

public class Modulos implements Comparable<Modulos>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private String cod_mod;
    private String nom_mod;
    private String num_seq;
    private String per_mod;

    public Modulos() {
    }

    public Modulos(String cod_mod, String nom_mod, String num_seq, String per_mod) {
        this.cod_mod = cod_mod;
        this.nom_mod = nom_mod;
        this.num_seq = num_seq;
        this.per_mod = per_mod;
    }

    public String getCod_mod() {
        return cod_mod;
    }

    public void setCod_mod(String cod_mod) {
        this.cod_mod = cod_mod;
    }

    public String getNom_mod() {
        return nom_mod;
    }

    public void setNom_mod(String nom_mod) {
        this.nom_mod = nom_mod;
    }

    public String getNum_seq() {
        return num_seq;
    }

    public void setNum_seq(String num_seq) {
        this.num_seq = num_seq;
    }

    public String getPer_mod() {
        return per_mod;
    }

    public void setPer_mod(String per_mod) {
        this.per_mod = per_mod;
    }

    //Método de comparação do número de sequência do programa
    @Override
    public int compareTo(Modulos sequencia) {
        return this.num_seq.compareTo(sequencia.num_seq);
    }
}
