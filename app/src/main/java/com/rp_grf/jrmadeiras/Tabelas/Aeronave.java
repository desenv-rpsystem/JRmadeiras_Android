package com.rp_grf.jrmadeiras.Tabelas;

public class Aeronave implements Comparable<Aeronave>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_bem;
    private String pre_bem;
    private String nom_bem;
    private String tip_bem;

    public Aeronave() {
    }

    public Aeronave(Long cod_bem, String pre_bem, String nom_bem, String tip_bem) {
        this.cod_bem = cod_bem;
        this.pre_bem = pre_bem;
        this.nom_bem = nom_bem;
        this.tip_bem = tip_bem;
    }

    public Long getCod_bem() {
        return cod_bem;
    }

    public void setCod_bem(Long cod_bem) {
        this.cod_bem = cod_bem;
    }

    public String getPre_bem() {
        return pre_bem;
    }

    public void setPre_bem(String pre_bem) {
        this.pre_bem = pre_bem;
    }

    public String getNom_bem() {
        return nom_bem;
    }

    public void setNom_bem(String nom_bem) {
        this.nom_bem = nom_bem;
    }

    public String getTip_bem() {
        return tip_bem;
    }

    public void setTip_bem(String tip_bem) {
        this.tip_bem = tip_bem;
    }

    @Override
    public int compareTo(Aeronave codigo) {
        return this.cod_bem.compareTo(codigo.cod_bem);
    }
}
