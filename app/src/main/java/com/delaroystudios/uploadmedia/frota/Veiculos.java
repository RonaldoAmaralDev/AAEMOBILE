package com.delaroystudios.uploadmedia.frota;

import java.io.Serializable;

public class Veiculos implements Serializable {

    private String id;
    private String centrocusto;
    private String colaborador;
    private  String fabricante;
    private String cidade;
    private String estado;
    private String placa;
    private String placa_anterior;
    private String descricao;
    private String modelo;
    private String kminicial;
    private String ativo;

    public Veiculos() {

    }
    public Veiculos(String id, String centrocusto, String colaborador, String fabricante, String cidade, String estado, String placa, String placa_anterior, String descricao, String modelo, String kminicial, String ativo) {
        this.id = id;
        this.centrocusto = centrocusto;
        this.colaborador = colaborador;
        this.fabricante = fabricante;
        this.cidade = cidade;
        this.estado = estado;
        this.placa = placa;
        this.placa_anterior = placa_anterior;
        this.descricao = descricao;
        this.modelo = modelo;
        this.kminicial = kminicial;
        this.ativo = ativo;

    }

    public String getId() {
        return id;
    }
    public String getCentrocusto() { return centrocusto;}
    public String getColaborador() { return  colaborador;}
    public String getFabricante() { return fabricante; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getPlaca() { return placa; }
    public String getPlaca_anterior() {
        return placa_anterior;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getModelo() { return modelo; }
    public String getKminicial() { return kminicial; }
    public String getAtivo() { return ativo;}


    public void setId(String id) {
        this.id = id;
    }
    public void setCentrocusto(String centrocusto) { this.centrocusto = centrocusto;}
    public void setColaborador(String colaborador) { this.colaborador = colaborador;}
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setKminicial(String kminicial) {
        this.kminicial = kminicial;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setPlaca_anterior(String placa_anterior) {
        this.placa_anterior = placa_anterior;
    }
}
