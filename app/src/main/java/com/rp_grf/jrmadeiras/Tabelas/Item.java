package com.rp_grf.jrmadeiras.Tabelas;

public class Item implements Comparable<Item>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_ite;
    private String nom_ite;
    private String ref_ite;
    private String nom_mar;
    private String cod_uni;
    private String val_uni;
    private String qtd_est;

    public Item() {
    }

    public Item(Long cod_ite, String nom_ite, String ref_ite, String nom_mar, String cod_uni,
                String val_uni, String qtd_est) {
        this.cod_ite = cod_ite;
        this.nom_ite = nom_ite;
        this.ref_ite = ref_ite;
        this.nom_mar = nom_mar;
        this.cod_uni = cod_uni;
        this.val_uni = val_uni;
        this.qtd_est = qtd_est;
    }

    public Long getCod_ite() {
        return cod_ite;
    }

    public void setCod_ite(Long cod_ite) {
        this.cod_ite = cod_ite;
    }

    public String getNom_ite() {
        return nom_ite;
    }

    public void setNom_ite(String nom_ite) {
        this.nom_ite = nom_ite;
    }

    public String getRef_ite() {
        return ref_ite;
    }

    public void setRef_ite(String ref_ite) {
        this.ref_ite = ref_ite;
    }

    public String getNom_mar() {
        return nom_mar;
    }

    public void setNom_mar(String nom_mar) {
        this.nom_mar = nom_mar;
    }

    public String getCod_uni() {
        return cod_uni;
    }

    public void setCod_uni(String cod_uni) {
        this.cod_uni = cod_uni;
    }

    public String getVal_uni() {
        return val_uni;
    }

    public void setVal_uni(String val_uni) {
        this.val_uni = val_uni;
    }

    public String getQtd_est() {
        return qtd_est;
    }

    public void setQtd_est(String qtd_est) {
        this.qtd_est = qtd_est;
    }

    //Método de comparação do número de sequência dos itens
    @Override
    public int compareTo(Item codigo) {
        return this.cod_ite.compareTo(codigo.cod_ite);
    }
}
