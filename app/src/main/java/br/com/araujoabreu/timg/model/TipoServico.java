package br.com.araujoabreu.timg.model;

import java.io.Serializable;

public class TipoServico implements Serializable {

    private String id;
    private  String descricao;


    public TipoServico() {

    }

    public TipoServico(String id, String descricao) {
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
