package com.rp_grf.jrmadeiras.Tabelas;

public class Romaneio_lib {

    private String num_rom;
    private String dat_ent;
    private String hor_ent;
    private String usu_ent;
    private String mod_cel;
    private String flg_can;
    private String dat_can;
    private String hor_can;
    private String usu_can;

    public Romaneio_lib() {
    }

    public Romaneio_lib(String num_rom, String dat_ent, String hor_ent, String usu_ent, String mod_cel, String flg_can, String dat_can, String hor_can, String usu_can) {
        this.num_rom = num_rom;
        this.dat_ent = dat_ent;
        this.hor_ent = hor_ent;
        this.usu_ent = usu_ent;
        this.mod_cel = mod_cel;
        this.flg_can = flg_can;
        this.dat_can = dat_can;
        this.hor_can = hor_can;
        this.usu_can = usu_can;
    }

    public String getNum_rom() {
        return num_rom;
    }

    public void setNum_rom(String num_rom) {
        this.num_rom = num_rom;
    }

    public String getDat_ent() {
        return dat_ent;
    }

    public void setDat_ent(String dat_ent) {
        this.dat_ent = dat_ent;
    }

    public String getHor_ent() {
        return hor_ent;
    }

    public void setHor_ent(String hor_ent) {
        this.hor_ent = hor_ent;
    }

    public String getUsu_ent() {
        return usu_ent;
    }

    public void setUsu_ent(String usu_ent) {
        this.usu_ent = usu_ent;
    }

    public String getMod_cel() {
        return mod_cel;
    }

    public void setMod_cel(String mod_cel) {
        this.mod_cel = mod_cel;
    }

    public String getFlg_can() {
        return flg_can;
    }

    public void setFlg_can(String flg_can) {
        this.flg_can = flg_can;
    }

    public String getDat_can() {
        return dat_can;
    }

    public void setDat_can(String dat_can) {
        this.dat_can = dat_can;
    }

    public String getHor_can() {
        return hor_can;
    }

    public void setHor_can(String hor_can) {
        this.hor_can = hor_can;
    }

    public String getUsu_can() {
        return usu_can;
    }

    public void setUsu_can(String usu_can) {
        this.usu_can = usu_can;
    }
}
