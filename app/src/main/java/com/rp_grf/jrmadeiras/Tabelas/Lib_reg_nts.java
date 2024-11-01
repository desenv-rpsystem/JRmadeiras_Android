package com.rp_grf.jrmadeiras.Tabelas;

public class Lib_reg_nts implements Comparable<Lib_reg_nts>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long num_reg_nts;
    private Long cod_est;
    private String cod_ser;
    private String num_doc;
    private String cod_cli;
    private String cod_ven;
    private String val_cus_tot;
    private String val_ven_tot;
    private String val_fre;
    private String val_des_fin;
    private String por_mar;
    private String dat_lib_doc;
    private String hor_lib_doc;
    private String usu_lib_doc;
    private String mar_equ;
    private String mod_equ;
    private String and_equ;

    public Lib_reg_nts() {
    }

    public Lib_reg_nts(Long num_reg_nts, Long cod_est, String cod_ser, String num_doc,
                       String cod_cli, String cod_ven, String val_cus_tot, String val_ven_tot,
                       String val_fre, String val_des_fin, String por_mar, String dat_lib_doc,
                       String hor_lib_doc, String usu_lib_doc, String mar_equ, String mod_equ,
                       String and_equ) {
        this.num_reg_nts = num_reg_nts;
        this.cod_est = cod_est;
        this.cod_ser = cod_ser;
        this.num_doc = num_doc;
        this.cod_cli = cod_cli;
        this.cod_ven = cod_ven;
        this.val_cus_tot = val_cus_tot;
        this.val_ven_tot = val_ven_tot;
        this.val_fre = val_fre;
        this.val_des_fin = val_des_fin;
        this.por_mar = por_mar;
        this.dat_lib_doc = dat_lib_doc;
        this.hor_lib_doc = hor_lib_doc;
        this.usu_lib_doc = usu_lib_doc;
        this.mar_equ = mar_equ;
        this.mod_equ = mod_equ;
        this.and_equ = and_equ;
    }

    public Long getNum_reg_nts() {
        return num_reg_nts;
    }

    public void setNum_reg_nts(Long num_reg_nts) {
        this.num_reg_nts = num_reg_nts;
    }

    public Long getCod_est() {
        return cod_est;
    }

    public void setCod_est(Long cod_est) {
        this.cod_est = cod_est;
    }

    public String getCod_ser() {
        return cod_ser;
    }

    public void setCod_ser(String cod_ser) {
        this.cod_ser = cod_ser;
    }

    public String getNum_doc() {
        return num_doc;
    }

    public void setNum_doc(String num_doc) {
        this.num_doc = num_doc;
    }

    public String getCod_cli() {
        return cod_cli;
    }

    public void setCod_cli(String cod_cli) {
        this.cod_cli = cod_cli;
    }

    public String getCod_ven() {
        return cod_ven;
    }

    public void setCod_ven(String cod_ven) {
        this.cod_ven = cod_ven;
    }

    public String getVal_cus_tot() {
        return val_cus_tot;
    }

    public void setVal_cus_tot(String val_cus_tot) {
        this.val_cus_tot = val_cus_tot;
    }

    public String getVal_ven_tot() {
        return val_ven_tot;
    }

    public void setVal_ven_tot(String val_ven_tot) {
        this.val_ven_tot = val_ven_tot;
    }

    public String getVal_fre() {
        return val_fre;
    }

    public void setVal_fre(String val_fre) {
        this.val_fre = val_fre;
    }

    public String getVal_des_fin() {
        return val_des_fin;
    }

    public void setVal_des_fin(String val_des_fin) {
        this.val_des_fin = val_des_fin;
    }

    public String getPor_mar() {
        return por_mar;
    }

    public void setPor_mar(String por_mar) {
        this.por_mar = por_mar;
    }

    public String getDat_lib_doc() {
        return dat_lib_doc;
    }

    public void setDat_lib_doc(String dat_lib_doc) {
        this.dat_lib_doc = dat_lib_doc;
    }

    public String getHor_lib_doc() {
        return hor_lib_doc;
    }

    public void setHor_lib_doc(String hor_lib_doc) {
        this.hor_lib_doc = hor_lib_doc;
    }

    public String getUsu_lib_doc() {
        return usu_lib_doc;
    }

    public void setUsu_lib_doc(String usu_lib_doc) {
        this.usu_lib_doc = usu_lib_doc;
    }

    public String getMar_equ() {
        return mar_equ;
    }

    public void setMar_equ(String mar_equ) {
        this.mar_equ = mar_equ;
    }

    public String getMod_equ() {
        return mod_equ;
    }

    public void setMod_equ(String mod_equ) {
        this.mod_equ = mod_equ;
    }

    public String getAnd_equ() {
        return and_equ;
    }

    public void setAnd_equ(String and_equ) {
        this.and_equ = and_equ;
    }

    //Método de comparação do número de sequência dos registros
    @Override
    public int compareTo(Lib_reg_nts registro) {
        return this.num_reg_nts.compareTo(registro.num_reg_nts);
    }
}
