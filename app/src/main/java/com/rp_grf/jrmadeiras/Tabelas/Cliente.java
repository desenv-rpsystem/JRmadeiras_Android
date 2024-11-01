package com.rp_grf.jrmadeiras.Tabelas;

public class Cliente implements Comparable<Cliente>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_cli;
    private String nom_cli;
    private String nom_fan_cli;
    private String cod_alt;
    private String dat_exa_med;

    public Cliente() {
    }

    public Cliente(Long cod_cli, String nom_cli, String nom_fan_cli, String cod_alt, String dat_exa_med) {
        this.cod_cli = cod_cli;
        this.nom_cli = nom_cli;
        this.nom_fan_cli = nom_fan_cli;
        this.cod_alt = cod_alt;
        this.dat_exa_med = dat_exa_med;
    }

    public Long getCod_cli() {
        return cod_cli;
    }

    public void setCod_cli(Long cod_cli) {
        this.cod_cli = cod_cli;
    }

    public String getNom_cli() {
        return nom_cli;
    }

    public void setNom_cli(String nom_cli) {
        this.nom_cli = nom_cli;
    }

    public String getNom_fan_cli() {
        return nom_fan_cli;
    }

    public void setNom_fan_cli(String nom_fan_cli) {
        this.nom_fan_cli = nom_fan_cli;
    }

    public String getCod_alt() {
        return cod_alt;
    }

    public void setCod_alt(String cod_alt) {
        this.cod_alt = cod_alt;
    }

    public String getDat_exa_med() {
        return dat_exa_med;
    }

    public void setDat_exa_med(String dat_exa_med) {
        this.dat_exa_med = dat_exa_med;
    }

    @Override
    public int compareTo(Cliente codigo) {
        return this.cod_cli.compareTo(codigo.cod_cli);
    }
}
