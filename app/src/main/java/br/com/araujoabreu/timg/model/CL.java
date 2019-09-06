package br.com.araujoabreu.timg.model;

import java.io.Serializable;

public class CL implements Serializable {

    private String idCL;
    private String centrocusto;
    private String descricao;
    public CL() {


    }

    public CL(String idCL, String centrocusto, String descricao) {
        this.idCL = idCL;
        this.centrocusto = centrocusto;
        this.descricao = descricao;

    }

    public String getIdCL() { return  idCL;}

    public String getCentrocusto() {
        return centrocusto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setId(String idCL) {
        this.idCL = idCL;
    }
    public void setCentrocusto(String centrocusto) {
        this.centrocusto = centrocusto;
    }
    public void setDescricao(String descricao) { this.descricao = descricao; }



}
