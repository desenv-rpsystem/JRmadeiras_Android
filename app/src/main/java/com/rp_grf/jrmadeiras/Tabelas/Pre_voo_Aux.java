package com.rp_grf.jrmadeiras.Tabelas;

public class Pre_voo_Aux implements Comparable<Pre_voo_Aux>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_pre_voo;
    private String nom_cli;
    private String pre_bem;
    private String nom_tip_voo;
    private String dat_mov_agv;
    private String per_ini_agv;

    public Pre_voo_Aux() {
    }

    public Pre_voo_Aux(Long cod_pre_voo, String nom_cli, String pre_bem, String nom_tip_voo, String dat_mov_agv, String per_ini_agv) {
        this.cod_pre_voo = cod_pre_voo;
        this.nom_cli = nom_cli;
        this.pre_bem = pre_bem;
        this.nom_tip_voo = nom_tip_voo;
        this.dat_mov_agv = dat_mov_agv;
        this.per_ini_agv = per_ini_agv;
    }

    public Long getCod_pre_voo() {
        return cod_pre_voo;
    }

    public void setCod_pre_voo(Long cod_pre_voo) {
        this.cod_pre_voo = cod_pre_voo;
    }

    public String getNom_cli() {
        return nom_cli;
    }

    public void setNom_cli(String nom_cli) {
        this.nom_cli = nom_cli;
    }

    public String getPre_bem() {
        return pre_bem;
    }

    public void setPre_bem(String pre_bem) {
        this.pre_bem = pre_bem;
    }

    public String getNom_tip_voo() {
        return nom_tip_voo;
    }

    public void setNom_tip_voo(String nom_tip_voo) {
        this.nom_tip_voo = nom_tip_voo;
    }

    public String getDat_mov_agv() {
        return dat_mov_agv;
    }

    public void setDat_mov_agv(String dat_mov_agv) {
        this.dat_mov_agv = dat_mov_agv;
    }

    public String getPer_ini_agv() {
        return per_ini_agv;
    }

    public void setPer_ini_agv(String per_ini_agv) {
        this.per_ini_agv = per_ini_agv;
    }

    @Override
    public int compareTo(Pre_voo_Aux codigo) {
        return this.cod_pre_voo.compareTo(codigo.cod_pre_voo);
    }
}
