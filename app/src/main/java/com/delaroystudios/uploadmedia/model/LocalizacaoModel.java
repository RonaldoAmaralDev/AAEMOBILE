package com.delaroystudios.uploadmedia.model;

import java.io.Serializable;

public class LocalizacaoModel implements Serializable {

    private String objetoID;
    private String nome;
    private String latitude;
    private  String longitude;

    public LocalizacaoModel() {


    }

    public LocalizacaoModel(String objetoID, String nome, String latitude, String longitude) {
        this.objetoID = objetoID;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getObjetoID() { return objetoID;}

    public String getNome() { return nome; }

    public String getLatitude() { return  latitude;}

    public String getLongitude() { return longitude; }


    public void setObjetoID(String objetoID) { this.objetoID = objetoID;}
    public void setNome(String nome) { this.nome = nome;}
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(String longitude) { this.longitude = longitude;}

}
