package com.rp_grf.jrmadeiras.Tabelas;

public class Versao {

    private String cod_ver;
    private String url_dow;

    public Versao() {
    }

    public Versao(String cod_ver, String url_dow) {
        this.cod_ver = cod_ver;
        this.url_dow = url_dow;
    }

    public String getCod_ver() {
        return cod_ver;
    }

    public void setCod_ver(String cod_ver) {
        this.cod_ver = cod_ver;
    }

    public String getUrl_dow() {
        return url_dow;
    }

    public void setUrl_dow(String url_dow) {
        this.url_dow = url_dow;
    }
}
