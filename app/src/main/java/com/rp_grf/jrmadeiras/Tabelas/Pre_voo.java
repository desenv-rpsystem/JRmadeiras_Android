package com.rp_grf.jrmadeiras.Tabelas;

public class Pre_voo implements Comparable<Pre_voo>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_pre_voo;
    private Long cod_age;
    private Long cod_cli;
    private String nom_cli;
    private Long cod_bem;
    private String pre_bem;
    private Long cod_tip_voo;
    private String nom_tip_voo;
    private Long cod_aer_ori;
    private String sig_aer_ori;
    private Long cod_aer_des;
    private String sig_aer_des;
    private String cod_cli_ins;
    private String dat_mov_agv;
    private String per_ini_agv;

    public Pre_voo() {
    }

    public Pre_voo(Long cod_pre_voo, Long cod_age, Long cod_cli, String nom_cli, Long cod_bem, String pre_bem, Long cod_tip_voo, String nom_tip_voo, Long cod_aer_ori, String sig_aer_ori, Long cod_aer_des, String sig_aer_des, String cod_cli_ins, String dat_mov_agv, String per_ini_agv) {
        this.cod_pre_voo = cod_pre_voo;
        this.cod_age = cod_age;
        this.cod_cli = cod_cli;
        this.nom_cli = nom_cli;
        this.cod_bem = cod_bem;
        this.pre_bem = pre_bem;
        this.cod_tip_voo = cod_tip_voo;
        this.nom_tip_voo = nom_tip_voo;
        this.cod_aer_ori = cod_aer_ori;
        this.sig_aer_ori = sig_aer_ori;
        this.cod_aer_des = cod_aer_des;
        this.sig_aer_des = sig_aer_des;
        this.cod_cli_ins = cod_cli_ins;
        this.dat_mov_agv = dat_mov_agv;
        this.per_ini_agv = per_ini_agv;
    }

    public Long getCod_pre_voo() {
        return cod_pre_voo;
    }

    public void setCod_pre_voo(Long cod_pre_voo) {
        this.cod_pre_voo = cod_pre_voo;
    }

    public Long getCod_age() {
        return cod_age;
    }

    public void setCod_age(Long cod_age) {
        this.cod_age = cod_age;
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

    public Long getCod_aer_ori() {
        return cod_aer_ori;
    }

    public void setCod_aer_ori(Long cod_aer_ori) {
        this.cod_aer_ori = cod_aer_ori;
    }

    public String getSig_aer_ori() {
        return sig_aer_ori;
    }

    public void setSig_aer_ori(String sig_aer_ori) {
        this.sig_aer_ori = sig_aer_ori;
    }

    public Long getCod_aer_des() {
        return cod_aer_des;
    }

    public void setCod_aer_des(Long cod_aer_des) {
        this.cod_aer_des = cod_aer_des;
    }

    public String getSig_aer_des() {
        return sig_aer_des;
    }

    public void setSig_aer_des(String sig_aer_des) {
        this.sig_aer_des = sig_aer_des;
    }

    public String getCod_cli_ins() {
        return cod_cli_ins;
    }

    public void setCod_cli_ins(String cod_cli_ins) {
        this.cod_cli_ins = cod_cli_ins;
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
    public int compareTo(Pre_voo codigo) {
        return this.cod_pre_voo.compareTo(codigo.cod_pre_voo);
    }
}
