package com.rp_grf.jrmadeiras.Tabelas;

public class Item_dep implements Comparable<Item_dep>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_ite;
    private Long cod_dep;
    private String qtd_est;
    private String nom_dep;

    public Item_dep() {
    }

    public Item_dep(Long cod_ite, Long cod_dep, String qtd_est, String nom_dep) {
        this.cod_ite = cod_ite;
        this.cod_dep = cod_dep;
        this.qtd_est = qtd_est;
        this.nom_dep = nom_dep;
    }

    public Long getCod_ite() {
        return cod_ite;
    }

    public void setCod_ite(Long cod_ite) {
        this.cod_ite = cod_ite;
    }

    public Long getCod_dep() {
        return cod_dep;
    }

    public void setCod_dep(Long cod_dep) {
        this.cod_dep = cod_dep;
    }

    public String getQtd_est() {
        return qtd_est;
    }

    public void setQtd_est(String qtd_est) {
        this.qtd_est = qtd_est;
    }

    public String getNom_dep() {
        return nom_dep;
    }

    public void setNom_dep(String nom_dep) {
        this.nom_dep = nom_dep;
    }


    //Método de comparação do número de sequência do programa
    @Override
    public int compareTo(Item_dep codigo) {
        return this.cod_ite.compareTo(codigo.cod_ite);
    }
}
