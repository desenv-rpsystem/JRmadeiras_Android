package com.rp_grf.jrmadeiras.Tabelas;

public class Det_lib_reg_nts  implements Comparable<Det_lib_reg_nts>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long num_reg_nts;
    private Long cod_ite;
    private String nom_ite;
    private String qtd_ite;
    private String cod_uni;
    private String val_uni;
    private String val_tot_ite;
    private String por_mar;

    public Det_lib_reg_nts() {
    }

    public Det_lib_reg_nts(Long num_reg_nts, Long cod_ite, String nom_ite, String qtd_ite,
                           String cod_uni, String val_uni, String val_tot_ite, String por_mar) {
        this.num_reg_nts = num_reg_nts;
        this.cod_ite = cod_ite;
        this.nom_ite = nom_ite;
        this.qtd_ite = qtd_ite;
        this.cod_uni = cod_uni;
        this.val_uni = val_uni;
        this.val_tot_ite = val_tot_ite;
        this.por_mar = por_mar;
    }

    public Long getNum_reg_nts() {
        return num_reg_nts;
    }

    public void setNum_reg_nts(Long num_reg_nts) {
        this.num_reg_nts = num_reg_nts;
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

    public String getQtd_ite() {
        return qtd_ite;
    }

    public void setQtd_ite(String qtd_ite) {
        this.qtd_ite = qtd_ite;
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

    public String getVal_tot_ite() {
        return val_tot_ite;
    }

    public void setVal_tot_ite(String val_tot_ite) {
        this.val_tot_ite = val_tot_ite;
    }

    public String getPor_mar() {
        return por_mar;
    }

    public void setPor_mar(String por_mar) {
        this.por_mar = por_mar;
    }

    //Método de comparação do número de sequência dos registros
    @Override
    public int compareTo(Det_lib_reg_nts item) {
        return this.cod_ite.compareTo(item.cod_ite);
    }
}
