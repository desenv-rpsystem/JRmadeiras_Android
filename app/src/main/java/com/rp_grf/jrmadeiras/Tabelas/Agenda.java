package com.rp_grf.jrmadeiras.Tabelas;

public class Agenda {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_age;

    private Long cod_pre_voo;

    private Long cod_cli;
    private String cod_cli_ins;
    private Long cod_bem;
    private Long cod_tip_voo;
    private Long cod_aer_ori;
    private Long cod_aer_des;

    private String dat_reg_voo;
    private String hor_par;
    private String min_par;
    private String hor_dec;
    private String min_dec;
    private String hor_pou;
    private String min_pou;
    private String hor_cor;
    private String min_cor;
    private String hor_voo;
    private String min_voo;

    private String hor_ini_fli;
    private String hor_fin_fli;
    private String tmp_voo_fli;
    private String num_pou;

    private String lit_com;
    private String gal_com;

    private String hor_ini_hob;
    private String hor_fin_hob;
    private String tmp_voo_hob;

    private String hor_diu;
    private String min_diu;
    private String hor_not;
    private String min_not;

    private String hor_ifrr;
    private String min_ifrr;
    private String hor_ifrc;
    private String min_ifrc;
    private String hor_vfr;
    private String min_vfr;

    private String inf_dia_bor;
    private String sob_voo_mn;
    private String flg_nav;

    private String mod_dry_wet;

    private String obs_reg_voo;

    private String dat_lib_age;
    private String hor_lib_age;
    private String cod_esp_doc;

    private String tmp_nao_voo;

    public Agenda() {
    }

    public Agenda(Long cod_age, Long cod_pre_voo, Long cod_cli, String cod_cli_ins, Long cod_bem, Long cod_tip_voo, Long cod_aer_ori, Long cod_aer_des, String dat_reg_voo, String hor_par, String min_par, String hor_dec, String min_dec, String hor_pou, String min_pou, String hor_cor, String min_cor, String hor_voo, String min_voo, String hor_ini_fli, String hor_fin_fli, String tmp_voo_fli, String num_pou, String lit_com, String gal_com, String hor_ini_hob, String hor_fin_hob, String tmp_voo_hob, String hor_diu, String min_diu, String hor_not, String min_not, String hor_ifrr, String min_ifrr, String hor_ifrc, String min_ifrc, String hor_vfr, String min_vfr, String inf_dia_bor, String sob_voo_mn, String flg_nav, String mod_dry_wet, String obs_reg_voo, String dat_lib_age, String hor_lib_age, String cod_esp_doc, String tmp_nao_voo) {
        this.cod_age = cod_age;
        this.cod_pre_voo = cod_pre_voo;
        this.cod_cli = cod_cli;
        this.cod_cli_ins = cod_cli_ins;
        this.cod_bem = cod_bem;
        this.cod_tip_voo = cod_tip_voo;
        this.cod_aer_ori = cod_aer_ori;
        this.cod_aer_des = cod_aer_des;
        this.dat_reg_voo = dat_reg_voo;
        this.hor_par = hor_par;
        this.min_par = min_par;
        this.hor_dec = hor_dec;
        this.min_dec = min_dec;
        this.hor_pou = hor_pou;
        this.min_pou = min_pou;
        this.hor_cor = hor_cor;
        this.min_cor = min_cor;
        this.hor_voo = hor_voo;
        this.min_voo = min_voo;
        this.hor_ini_fli = hor_ini_fli;
        this.hor_fin_fli = hor_fin_fli;
        this.tmp_voo_fli = tmp_voo_fli;
        this.num_pou = num_pou;
        this.lit_com = lit_com;
        this.gal_com = gal_com;
        this.hor_ini_hob = hor_ini_hob;
        this.hor_fin_hob = hor_fin_hob;
        this.tmp_voo_hob = tmp_voo_hob;
        this.hor_diu = hor_diu;
        this.min_diu = min_diu;
        this.hor_not = hor_not;
        this.min_not = min_not;
        this.hor_ifrr = hor_ifrr;
        this.min_ifrr = min_ifrr;
        this.hor_ifrc = hor_ifrc;
        this.min_ifrc = min_ifrc;
        this.hor_vfr = hor_vfr;
        this.min_vfr = min_vfr;
        this.inf_dia_bor = inf_dia_bor;
        this.sob_voo_mn = sob_voo_mn;
        this.flg_nav = flg_nav;
        this.mod_dry_wet = mod_dry_wet;
        this.obs_reg_voo = obs_reg_voo;
        this.dat_lib_age = dat_lib_age;
        this.hor_lib_age = hor_lib_age;
        this.cod_esp_doc = cod_esp_doc;
        this.tmp_nao_voo = tmp_nao_voo;
    }

    public Long getCod_age() {
        return cod_age;
    }

    public void setCod_age(Long cod_age) {
        this.cod_age = cod_age;
    }

    public Long getCod_pre_voo() {
        return cod_pre_voo;
    }

    public void setCod_pre_voo(Long cod_pre_voo) {
        this.cod_pre_voo = cod_pre_voo;
    }

    public Long getCod_cli() {
        return cod_cli;
    }

    public void setCod_cli(Long cod_cli) {
        this.cod_cli = cod_cli;
    }

    public String getCod_cli_ins() {
        return cod_cli_ins;
    }

    public void setCod_cli_ins(String cod_cli_ins) {
        this.cod_cli_ins = cod_cli_ins;
    }

    public Long getCod_bem() {
        return cod_bem;
    }

    public void setCod_bem(Long cod_bem) {
        this.cod_bem = cod_bem;
    }

    public Long getCod_tip_voo() {
        return cod_tip_voo;
    }

    public void setCod_tip_voo(Long cod_tip_voo) {
        this.cod_tip_voo = cod_tip_voo;
    }

    public Long getCod_aer_ori() {
        return cod_aer_ori;
    }

    public void setCod_aer_ori(Long cod_aer_ori) {
        this.cod_aer_ori = cod_aer_ori;
    }

    public Long getCod_aer_des() {
        return cod_aer_des;
    }

    public void setCod_aer_des(Long cod_aer_des) {
        this.cod_aer_des = cod_aer_des;
    }

    public String getDat_reg_voo() {
        return dat_reg_voo;
    }

    public void setDat_reg_voo(String dat_reg_voo) {
        this.dat_reg_voo = dat_reg_voo;
    }

    public String getHor_par() {
        return hor_par;
    }

    public void setHor_par(String hor_par) {
        this.hor_par = hor_par;
    }

    public String getMin_par() {
        return min_par;
    }

    public void setMin_par(String min_par) {
        this.min_par = min_par;
    }

    public String getHor_dec() {
        return hor_dec;
    }

    public void setHor_dec(String hor_dec) {
        this.hor_dec = hor_dec;
    }

    public String getMin_dec() {
        return min_dec;
    }

    public void setMin_dec(String min_dec) {
        this.min_dec = min_dec;
    }

    public String getHor_pou() {
        return hor_pou;
    }

    public void setHor_pou(String hor_pou) {
        this.hor_pou = hor_pou;
    }

    public String getMin_pou() {
        return min_pou;
    }

    public void setMin_pou(String min_pou) {
        this.min_pou = min_pou;
    }

    public String getHor_cor() {
        return hor_cor;
    }

    public void setHor_cor(String hor_cor) {
        this.hor_cor = hor_cor;
    }

    public String getMin_cor() {
        return min_cor;
    }

    public void setMin_cor(String min_cor) {
        this.min_cor = min_cor;
    }

    public String getHor_voo() {
        return hor_voo;
    }

    public void setHor_voo(String hor_voo) {
        this.hor_voo = hor_voo;
    }

    public String getMin_voo() {
        return min_voo;
    }

    public void setMin_voo(String min_voo) {
        this.min_voo = min_voo;
    }

    public String getHor_ini_fli() {
        return hor_ini_fli;
    }

    public void setHor_ini_fli(String hor_ini_fli) {
        this.hor_ini_fli = hor_ini_fli;
    }

    public String getHor_fin_fli() {
        return hor_fin_fli;
    }

    public void setHor_fin_fli(String hor_fin_fli) {
        this.hor_fin_fli = hor_fin_fli;
    }

    public String getTmp_voo_fli() {
        return tmp_voo_fli;
    }

    public void setTmp_voo_fli(String tmp_voo_fli) {
        this.tmp_voo_fli = tmp_voo_fli;
    }

    public String getNum_pou() {
        return num_pou;
    }

    public void setNum_pou(String num_pou) {
        this.num_pou = num_pou;
    }

    public String getLit_com() {
        return lit_com;
    }

    public void setLit_com(String lit_com) {
        this.lit_com = lit_com;
    }

    public String getGal_com() {
        return gal_com;
    }

    public void setGal_com(String gal_com) {
        this.gal_com = gal_com;
    }

    public String getHor_ini_hob() {
        return hor_ini_hob;
    }

    public void setHor_ini_hob(String hor_ini_hob) {
        this.hor_ini_hob = hor_ini_hob;
    }

    public String getHor_fin_hob() {
        return hor_fin_hob;
    }

    public void setHor_fin_hob(String hor_fin_hob) {
        this.hor_fin_hob = hor_fin_hob;
    }

    public String getTmp_voo_hob() {
        return tmp_voo_hob;
    }

    public void setTmp_voo_hob(String tmp_voo_hob) {
        this.tmp_voo_hob = tmp_voo_hob;
    }

    public String getHor_diu() {
        return hor_diu;
    }

    public void setHor_diu(String hor_diu) {
        this.hor_diu = hor_diu;
    }

    public String getMin_diu() {
        return min_diu;
    }

    public void setMin_diu(String min_diu) {
        this.min_diu = min_diu;
    }

    public String getHor_not() {
        return hor_not;
    }

    public void setHor_not(String hor_not) {
        this.hor_not = hor_not;
    }

    public String getMin_not() {
        return min_not;
    }

    public void setMin_not(String min_not) {
        this.min_not = min_not;
    }

    public String getHor_ifrr() {
        return hor_ifrr;
    }

    public void setHor_ifrr(String hor_ifrr) {
        this.hor_ifrr = hor_ifrr;
    }

    public String getMin_ifrr() {
        return min_ifrr;
    }

    public void setMin_ifrr(String min_ifrr) {
        this.min_ifrr = min_ifrr;
    }

    public String getHor_ifrc() {
        return hor_ifrc;
    }

    public void setHor_ifrc(String hor_ifrc) {
        this.hor_ifrc = hor_ifrc;
    }

    public String getMin_ifrc() {
        return min_ifrc;
    }

    public void setMin_ifrc(String min_ifrc) {
        this.min_ifrc = min_ifrc;
    }

    public String getHor_vfr() {
        return hor_vfr;
    }

    public void setHor_vfr(String hor_vfr) {
        this.hor_vfr = hor_vfr;
    }

    public String getMin_vfr() {
        return min_vfr;
    }

    public void setMin_vfr(String min_vfr) {
        this.min_vfr = min_vfr;
    }

    public String getInf_dia_bor() {
        return inf_dia_bor;
    }

    public void setInf_dia_bor(String inf_dia_bor) {
        this.inf_dia_bor = inf_dia_bor;
    }

    public String getSob_voo_mn() {
        return sob_voo_mn;
    }

    public void setSob_voo_mn(String sob_voo_mn) {
        this.sob_voo_mn = sob_voo_mn;
    }

    public String getFlg_nav() {
        return flg_nav;
    }

    public void setFlg_nav(String flg_nav) {
        this.flg_nav = flg_nav;
    }

    public String getMod_dry_wet() {
        return mod_dry_wet;
    }

    public void setMod_dry_wet(String mod_dry_wet) {
        this.mod_dry_wet = mod_dry_wet;
    }

    public String getObs_reg_voo() {
        return obs_reg_voo;
    }

    public void setObs_reg_voo(String obs_reg_voo) {
        this.obs_reg_voo = obs_reg_voo;
    }

    public String getDat_lib_age() {
        return dat_lib_age;
    }

    public void setDat_lib_age(String dat_lib_age) {
        this.dat_lib_age = dat_lib_age;
    }

    public String getHor_lib_age() {
        return hor_lib_age;
    }

    public void setHor_lib_age(String hor_lib_age) {
        this.hor_lib_age = hor_lib_age;
    }

    public String getCod_esp_doc() {
        return cod_esp_doc;
    }

    public void setCod_esp_doc(String cod_esp_doc) {
        this.cod_esp_doc = cod_esp_doc;
    }

    public String getTmp_nao_voo() {
        return tmp_nao_voo;
    }

    public void setTmp_nao_voo(String tmp_nao_voo) {
        this.tmp_nao_voo = tmp_nao_voo;
    }
}