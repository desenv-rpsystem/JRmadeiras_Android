package com.rp_grf.jrmadeiras.Tabelas;

public class Instrutor implements Comparable<Instrutor>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_cli_ins;
    private String nom_cli_ins;
    private String nom_fan_cli_ins;

    public Instrutor() {
    }

    public Instrutor(Long cod_cli_ins, String nom_cli_ins, String nom_fan_cli_ins) {
        this.cod_cli_ins = cod_cli_ins;
        this.nom_cli_ins = nom_cli_ins;
        this.nom_fan_cli_ins = nom_fan_cli_ins;
    }

    public Long getCod_cli_ins() {
        return cod_cli_ins;
    }

    public void setCod_cli_ins(Long cod_cli_ins) {
        this.cod_cli_ins = cod_cli_ins;
    }

    public String getNom_cli_ins() {
        return nom_cli_ins;
    }

    public void setNom_cli_ins(String nom_cli_ins) {
        this.nom_cli_ins = nom_cli_ins;
    }

    public String getNom_fan_cli_ins() {
        return nom_fan_cli_ins;
    }

    public void setNom_fan_cli_ins(String nom_fan_cli_ins) {
        this.nom_fan_cli_ins = nom_fan_cli_ins;
    }

    @Override
    public int compareTo(Instrutor codigo) {
        return this.cod_cli_ins.compareTo(codigo.cod_cli_ins);
    }
}
