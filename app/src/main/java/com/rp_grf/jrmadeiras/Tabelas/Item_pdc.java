package com.rp_grf.jrmadeiras.Tabelas;

public class Item_pdc implements Comparable<Item_pdc>, java.io.Serializable{

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_ite;
    private String num_pdc;
    private String dat_ent;
    private String qtd_pen;

    public Item_pdc() {
    }

    public Item_pdc(Long cod_ite, String num_pdc, String dat_ent, String qtd_pen) {
        this.cod_ite = cod_ite;
        this.num_pdc = num_pdc;
        this.dat_ent = dat_ent;
        this.qtd_pen = qtd_pen;
    }

    public Long getCod_ite() {
        return cod_ite;
    }

    public void setCod_ite(Long cod_ite) {
        this.cod_ite = cod_ite;
    }

    public String getNum_pdc() {
        return num_pdc;
    }

    public void setNum_pdc(String num_pdc) {
        this.num_pdc = num_pdc;
    }

    public String getDat_ent() {
        return dat_ent;
    }

    public void setDat_ent(String dat_ent) {
        this.dat_ent = dat_ent;
    }

    public String getQtd_pen() {
        return qtd_pen;
    }

    public void setQtd_pen(String qtd_pen) {
        this.qtd_pen = qtd_pen;
    }

    //Método de comparação do número de sequência do programa
    @Override
    public int compareTo(Item_pdc codigo) {
        return this.cod_ite.compareTo(codigo.cod_ite);
    }
}
