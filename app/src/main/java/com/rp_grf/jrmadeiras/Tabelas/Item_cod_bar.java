package com.rp_grf.jrmadeiras.Tabelas;

public class Item_cod_bar  implements Comparable<Item_cod_bar>, java.io.Serializable {

    //Campos da tabela - usar alt+insert para gerar o getter and setter -  atualizar o construtor

    private Long cod_ite;
    private String cod_bar_ite;

    public Item_cod_bar() {
    }

    public Item_cod_bar(Long cod_ite, String cod_bar_ite) {
        this.cod_ite = cod_ite;
        this.cod_bar_ite = cod_bar_ite;
    }

    public Long getCod_ite() {
        return cod_ite;
    }

    public void setCod_ite(Long cod_ite) {
        this.cod_ite = cod_ite;
    }

    public String getCod_bar_ite() {
        return cod_bar_ite;
    }

    public void setCod_bar_ite(String cod_bar_ite) {
        this.cod_bar_ite = cod_bar_ite;
    }

    //Método de comparação do número de sequência do programa
    @Override
    public int compareTo(Item_cod_bar codigo) {
        return this.cod_ite.compareTo(codigo.cod_ite);
    }
}
