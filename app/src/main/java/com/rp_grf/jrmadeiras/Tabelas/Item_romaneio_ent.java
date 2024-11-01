package com.rp_grf.jrmadeiras.Tabelas;

public class Item_romaneio_ent implements Comparable<Item_romaneio_ent>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private String num_rom;
    private String num_seq;
    private Long cod_ite;
    private Long num_reg_nts;
    private String nom_ite;
    private String qtd_ite;
    private String qtd_pen;
    private String cod_uni;
    private String cod_dep;
    private String nom_dep;
    private String cod_ide_loc;

    public Item_romaneio_ent(){

    }

    public Item_romaneio_ent(String num_rom, String num_seq, Long cod_ite, Long num_reg_nts, String nom_ite, String qtd_ite, String qtd_pen, String cod_uni, String cod_dep, String nom_dep, String cod_ide_loc) {
        this.num_rom = num_rom;
        this.num_seq = num_seq;
        this.cod_ite = cod_ite;
        this.num_reg_nts = num_reg_nts;
        this.nom_ite = nom_ite;
        this.qtd_ite = qtd_ite;
        this.qtd_pen = qtd_pen;
        this.cod_uni = cod_uni;
        this.cod_dep = cod_dep;
        this.nom_dep = nom_dep;
        this.cod_ide_loc = cod_ide_loc;
    }

    public String getNum_rom() {
        return num_rom;
    }

    public void setNum_rom(String num_rom) {
        this.num_rom = num_rom;
    }

    public String getNum_seq() {
        return num_seq;
    }

    public void setNum_seq(String num_seq) {
        this.num_seq = num_seq;
    }

    public Long getCod_ite() {
        return cod_ite;
    }

    public void setCod_ite(Long cod_ite) {
        this.cod_ite = cod_ite;
    }

    public Long getNum_reg_nts() {
        return num_reg_nts;
    }

    public void setNum_reg_nts(Long num_reg_nts) {
        this.num_reg_nts = num_reg_nts;
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

    public String getQtd_pen() {
        return qtd_pen;
    }

    public void setQtd_pen(String qtd_pen) {
        this.qtd_pen = qtd_pen;
    }

    public String getCod_uni() {
        return cod_uni;
    }

    public void setCod_uni(String cod_uni) {
        this.cod_uni = cod_uni;
    }

    public String getCod_dep() {
        return cod_dep;
    }

    public void setCod_dep(String cod_dep) {
        this.cod_dep = cod_dep;
    }

    public String getNom_dep() {
        return nom_dep;
    }

    public void setNom_dep(String nom_dep) {
        this.nom_dep = nom_dep;
    }

    public String getCod_ide_loc() {
        return cod_ide_loc;
    }

    public void setCod_ide_loc(String cod_ide_loc) {
        this.cod_ide_loc = cod_ide_loc;
    }

    @Override
    public int compareTo(Item_romaneio_ent localizacao) {
        return this.cod_ide_loc.compareTo(localizacao.cod_ide_loc);
    }

}
