package com.rp_grf.jrmadeiras.Tabelas;

public class Fotos {

    private String codigo_registro;
    private String nome_arquivo;
    private String caminho_foto;
    private String flag_cancelamento;

    public Fotos() {
    }

    public Fotos(String codigo_registro, String nome_arquivo, String caminho_foto, String flag_cancelamento) {
        this.codigo_registro = codigo_registro;
        this.nome_arquivo = nome_arquivo;
        this.caminho_foto = caminho_foto;
        this.flag_cancelamento = flag_cancelamento;
    }

    public String getCodigo_registro() {
        return codigo_registro;
    }

    public void setCodigo_registro(String codigo_registro) {
        this.codigo_registro = codigo_registro;
    }

    public String getNome_arquivo() {
        return nome_arquivo;
    }

    public void setNome_arquivo(String nome_arquivo) {
        this.nome_arquivo = nome_arquivo;
    }

    public String getCaminho_foto() {
        return caminho_foto;
    }

    public void setCaminho_foto(String caminho_foto) {
        this.caminho_foto = caminho_foto;
    }

    public String getFlag_cancelamento() {
        return flag_cancelamento;
    }

    public void setFlag_cancelamento(String flag_cancelamento) {
        this.flag_cancelamento = flag_cancelamento;
    }
}
