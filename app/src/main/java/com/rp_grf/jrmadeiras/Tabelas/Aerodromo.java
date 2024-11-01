package com.rp_grf.jrmadeiras.Tabelas;

public class Aerodromo implements Comparable<Aerodromo>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_aer;
    private String nom_aer;
    private String sig_aer;

    public Aerodromo() {
    }

    public Aerodromo(Long cod_aer, String nom_aer, String sig_aer) {
        this.cod_aer = cod_aer;
        this.nom_aer = nom_aer;
        this.sig_aer = sig_aer;
    }

    public Long getCod_aer() {
        return cod_aer;
    }

    public void setCod_aer(Long cod_aer) {
        this.cod_aer = cod_aer;
    }

    public String getNom_aer() {
        return nom_aer;
    }

    public void setNom_aer(String nom_aer) {
        this.nom_aer = nom_aer;
    }

    public String getSig_aer() {
        return sig_aer;
    }

    public void setSig_aer(String sig_aer) {
        this.sig_aer = sig_aer;
    }

    @Override
    public int compareTo(Aerodromo codigo) {
        return this.cod_aer.compareTo(codigo.cod_aer);
    }
}
