package com.rp_grf.jrmadeiras.Tabelas;

public class Tipo_voo implements Comparable<Tipo_voo>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_tip_voo;
    private String nom_tip_voo;

    public Tipo_voo() {
    }

    public Tipo_voo(Long cod_tip_voo, String nom_tip_voo) {
        this.cod_tip_voo = cod_tip_voo;
        this.nom_tip_voo = nom_tip_voo;
    }

    public Long getCod_tip_voo() {
        return cod_tip_voo;
    }

    public void setCod_tip_voo(Long cod_tip_voo) {
        this.cod_tip_voo = cod_tip_voo;
    }

    public String getNom_tip_voo() {
        return nom_tip_voo;
    }

    public void setNom_tip_voo(String nom_tip_voo) {
        this.nom_tip_voo = nom_tip_voo;
    }

    @Override
    public int compareTo(Tipo_voo codigo) {
        return this.cod_tip_voo.compareTo(codigo.cod_tip_voo);
    }
}
