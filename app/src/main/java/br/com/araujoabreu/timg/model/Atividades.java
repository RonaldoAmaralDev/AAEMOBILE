package br.com.araujoabreu.timg.model;

import java.io.Serializable;

public class Atividades implements Serializable {

    private String itenID;
    private  String itenchecklist;
    private String itenDescricao;


    public Atividades() {


    }

    public Atividades(String itenID, String itenchecklist, String itenDescricao) {
        this.itenID = itenID;
        this.itenchecklist = itenchecklist;
        this.itenDescricao = itenDescricao;

    }

    public String getItenID() { return  itenID;}

    public String getItenchecklist() { return itenchecklist; }

    public String getItenDescricao() {
        return itenDescricao;
    }

    public void setItenID(String itenID) {
        this.itenID = itenID;
    }
    public void setItenchecklist(String itenchecklist) { this.itenchecklist = itenchecklist;}
    public void setItenDescricao(String itenDescricao) {
        this.itenDescricao = itenDescricao;
    }

}
