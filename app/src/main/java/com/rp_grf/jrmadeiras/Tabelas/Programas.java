package com.rp_grf.jrmadeiras.Tabelas;

public class Programas implements Comparable<Programas>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private String cod_mod;
    private String cod_prg;
    private String des_prg;
    private String num_seq;
    private String per_prg;

    public Programas() {
    }

    public Programas(String cod_mod, String cod_prg, String des_prg, String num_seq, String per_prg) {
        this.cod_mod = cod_mod;
        this.cod_prg = cod_prg;
        this.des_prg = des_prg;
        this.num_seq = num_seq;
        this.per_prg = per_prg;
    }

    public String getCod_mod() {
        return cod_mod;
    }

    public void setCod_mod(String cod_mod) {
        this.cod_mod = cod_mod;
    }

    public String getCod_prg() {
        return cod_prg;
    }

    public void setCod_prg(String cod_prg) {
        this.cod_prg = cod_prg;
    }

    public String getDes_prg() {
        return des_prg;
    }

    public void setDes_prg(String des_prg) {
        this.des_prg = des_prg;
    }

    public String getNum_seq() {
        return num_seq;
    }

    public void setNum_seq(String num_seq) {
        this.num_seq = num_seq;
    }

    public String getPer_prg() {
        return per_prg;
    }

    public void setPer_prg(String per_prg) {
        this.per_prg = per_prg;
    }

    //Método de comparação do número de sequência do programa
    @Override
    public int compareTo(Programas sequencia) {
        return this.num_seq.compareTo(sequencia.num_seq);
    }
}
