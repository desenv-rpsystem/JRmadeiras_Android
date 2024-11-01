package com.rp_grf.jrmadeiras.Tabelas;

public class Romaneio_ent implements Comparable<Romaneio_ent>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private String num_rom;
    private Long num_seq;
    private Long num_reg_nts;
    private String cod_ser;
    private String num_doc;
    private String nom_cli;
    private String end_cli;
    private String num_end_cli;
    private String com_bai_cli;
    private String nom_mun;
    private String cod_uni_fed;
    private String cep_cli;
    private String fon_cli;
    private String fon_cli_2;
    private String nom_ven;
    private String dat_lib_re;
    private String usu_lib_re;
    private String hor_lib_re;
    private String map_lat;
    private String map_lon;
    private String dat_che_cli;
    private String hor_che_cli;

    public Romaneio_ent() {
    }

    public Romaneio_ent(String num_rom, Long num_seq, Long num_reg_nts, String cod_ser, String num_doc, String nom_cli, String end_cli, String num_end_cli, String com_bai_cli, String nom_mun, String cod_uni_fed, String cep_cli, String fon_cli, String fon_cli_2, String nom_ven, String dat_lib_re, String usu_lib_re, String hor_lib_re, String map_lat, String map_lon, String dat_che_cli, String hor_che_cli) {
        this.num_rom = num_rom;
        this.num_seq = num_seq;
        this.num_reg_nts = num_reg_nts;
        this.cod_ser = cod_ser;
        this.num_doc = num_doc;
        this.nom_cli = nom_cli;
        this.end_cli = end_cli;
        this.num_end_cli = num_end_cli;
        this.com_bai_cli = com_bai_cli;
        this.nom_mun = nom_mun;
        this.cod_uni_fed = cod_uni_fed;
        this.cep_cli = cep_cli;
        this.fon_cli = fon_cli;
        this.fon_cli_2 = fon_cli_2;
        this.nom_ven = nom_ven;
        this.dat_lib_re = dat_lib_re;
        this.usu_lib_re = usu_lib_re;
        this.hor_lib_re = hor_lib_re;
        this.map_lat = map_lat;
        this.map_lon = map_lon;
        this.dat_che_cli = dat_che_cli;
        this.hor_che_cli = hor_che_cli;
    }

    public String getNum_rom() {
        return num_rom;
    }

    public void setNum_rom(String num_rom) {
        this.num_rom = num_rom;
    }

    public Long getNum_seq() {
        return num_seq;
    }

    public void setNum_seq(Long num_seq) {
        this.num_seq = num_seq;
    }

    public Long getNum_reg_nts() {
        return num_reg_nts;
    }

    public void setNum_reg_nts(Long num_reg_nts) {
        this.num_reg_nts = num_reg_nts;
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

    public String getNom_cli() {
        return nom_cli;
    }

    public void setNom_cli(String nom_cli) {
        this.nom_cli = nom_cli;
    }

    public String getEnd_cli() {
        return end_cli;
    }

    public void setEnd_cli(String end_cli) {
        this.end_cli = end_cli;
    }

    public String getNum_end_cli() {
        return num_end_cli;
    }

    public void setNum_end_cli(String num_end_cli) {
        this.num_end_cli = num_end_cli;
    }

    public String getCom_bai_cli() {
        return com_bai_cli;
    }

    public void setCom_bai_cli(String com_bai_cli) {
        this.com_bai_cli = com_bai_cli;
    }

    public String getNom_mun() {
        return nom_mun;
    }

    public void setNom_mun(String nom_mun) {
        this.nom_mun = nom_mun;
    }

    public String getCod_uni_fed() {
        return cod_uni_fed;
    }

    public void setCod_uni_fed(String cod_uni_fed) {
        this.cod_uni_fed = cod_uni_fed;
    }

    public String getCep_cli() {
        return cep_cli;
    }

    public void setCep_cli(String cep_cli) {
        this.cep_cli = cep_cli;
    }

    public String getFon_cli() {
        return fon_cli;
    }

    public void setFon_cli(String fon_cli) {
        this.fon_cli = fon_cli;
    }

    public String getFon_cli_2() {
        return fon_cli_2;
    }

    public void setFon_cli_2(String fon_cli_2) {
        this.fon_cli_2 = fon_cli_2;
    }

    public String getNom_ven() {
        return nom_ven;
    }

    public void setNom_ven(String nom_ven) {
        this.nom_ven = nom_ven;
    }

    public String getDat_lib_re() {
        return dat_lib_re;
    }

    public void setDat_lib_re(String dat_lib_re) {
        this.dat_lib_re = dat_lib_re;
    }

    public String getUsu_lib_re() {
        return usu_lib_re;
    }

    public void setUsu_lib_re(String usu_lib_re) {
        this.usu_lib_re = usu_lib_re;
    }

    public String getHor_lib_re() {
        return hor_lib_re;
    }

    public void setHor_lib_re(String hor_lib_re) {
        this.hor_lib_re = hor_lib_re;
    }

    public String getMap_lat() {
        return map_lat;
    }

    public void setMap_lat(String map_lat) {
        this.map_lat = map_lat;
    }

    public String getMap_lon() {
        return map_lon;
    }

    public void setMap_lon(String map_lon) {
        this.map_lon = map_lon;
    }

    public String getDat_che_cli() {
        return dat_che_cli;
    }

    public void setDat_che_cli(String dat_che_cli) {
        this.dat_che_cli = dat_che_cli;
    }

    public String getHor_che_cli() {
        return hor_che_cli;
    }

    public void setHor_che_cli(String hor_che_cli) {
        this.hor_che_cli = hor_che_cli;
    }

    @Override
    public int compareTo(Romaneio_ent sequencia) {
        return this.num_seq.compareTo(sequencia.num_seq);
    }

}
