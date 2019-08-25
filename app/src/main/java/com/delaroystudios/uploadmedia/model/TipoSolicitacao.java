package com.delaroystudios.uploadmedia.model;

import java.io.Serializable;

public class TipoSolicitacao implements Serializable {

    private String id;
    private  String descricao;


    public TipoSolicitacao() {

    }

    public TipoSolicitacao(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;

    }

    public String getId() { return  id;}

    public String getDescricao() { return descricao; }


    public void setId(String id) {
        this.id = id;
    }
    public void setDescricao(String descricao) { this.descricao = descricao;}

}
