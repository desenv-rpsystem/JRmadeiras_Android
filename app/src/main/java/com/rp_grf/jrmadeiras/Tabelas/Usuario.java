package com.rp_grf.jrmadeiras.Tabelas;

public class Usuario {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private String cod_usu; //cod_usu
    private String sen_usu; //senha
    private String nom_usu; //nome do usuario

    public Usuario() {
    }

    public Usuario(String cod_usu, String sen_usu, String nom_usu) {
        this.cod_usu = cod_usu;
        this.sen_usu = sen_usu;
        this.nom_usu = nom_usu;
    }

    public String getCod_usu() {
        return cod_usu;
    }

    public void setCod_usu(String usuario) {
        this.cod_usu = usuario;
    }

    public String getSen_usu() {
        return sen_usu;
    }

    public void setSen_usu(String sen_usu) {
        this.sen_usu = sen_usu;
    }

    public String getNom_usu() {
        return nom_usu;
    }

    public void setNom_usu(String nom_usu) {
        this.nom_usu = nom_usu;
    }
}
