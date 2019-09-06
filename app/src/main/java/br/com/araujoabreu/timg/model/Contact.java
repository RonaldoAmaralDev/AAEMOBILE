package br.com.araujoabreu.timg.model;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Contact implements Serializable {



    private String id;
    private String codigolocal;
    private String centrocusto_id;
    private String descricaolocal;
    private String bairro;
    private String cidade;
    private String latitude;
    private String longitude;
    private String sigla;
    private String estado;
    private String tempogasto;
    private String regiaoID;
    private String regiaoDescricao;
    private String poloatendimentosID;
    private String poloatenidmentosDescricao;
    private String contatoID;
    private String contatoCLID;
    private String contatoNome;
    private String contatoEndereco;
    private String contatoLatitude;
    private String contatoLongitude;
    private String areaconstruida;
    private String areacapina;
    private String enderecolocal;
    private String frequencia;
    private String raio;
    private String situacao;

    public Contact() {
    }

    public Contact(String id, String codigolocal, String centrocusto_id, String descricaolocal,
                   String bairro, String cidade, String latitude, String longitude, String sigla, String estado,
                   String tempogasto, String regiaoID, String regiaoDescricao, String poloatendimentosID, String poloatenidmentosDescricao,
                   String contatoID, String contatoCLID, String contatoNome, String contatoEndereco,
                   String contatoLatitude, String contatoLongitude, String areaconstruida, String areacapina, String enderecolocal, String frequencia, String raio, String situacao) {
        this.id = id;
        this.codigolocal = codigolocal;
        this.centrocusto_id = centrocusto_id;
        this.descricaolocal = descricaolocal;
        this.bairro = bairro;
        this.cidade = cidade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sigla = sigla;
        this.estado = estado;
        this.tempogasto = tempogasto;
        this.regiaoID = regiaoID;
        this.regiaoDescricao = regiaoDescricao;
        this.poloatendimentosID = poloatendimentosID;
        this.poloatenidmentosDescricao = poloatenidmentosDescricao;
        this.contatoID = contatoID;
        this.contatoCLID = contatoCLID;
        this.contatoNome = contatoNome;
        this.contatoEndereco = contatoEndereco;
        this.contatoLatitude = contatoLatitude;
        this.contatoLongitude = contatoLongitude;
        this.areaconstruida = areaconstruida;
        this.areacapina = areacapina;
        this.enderecolocal = enderecolocal;
        this.frequencia = frequencia;
        this.raio = raio;
        this.situacao = situacao;
    }

    public String getId() { return  id;}

    public String getCodigolocal() {
        return codigolocal;
    }

    public String getCentrocusto_id() { return centrocusto_id; }

    public String getDescricaolocal() {
        return descricaolocal;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getLatitude() { return latitude;}

    public String getLongitude() { return longitude;}

    public String getSigla() { return sigla;}
    public String getEstado() { return estado;}
    public String getTempogasto() { return tempogasto;}
    public String getRegiaoID() { return regiaoID; }
    public String getRegiaoDescricao() { return regiaoDescricao;}
    public String getPoloatendimentosID() { return poloatendimentosID;}
    public String getPoloatenidmentosDescricao() { return poloatenidmentosDescricao;}
    public String getContatoID() { return contatoID;}
    public String getContatoCLID() { return contatoCLID;}
    public String getContatoNome() { return contatoNome;}
    public String getContatoEndereco() { return contatoEndereco;}
    public String getContatoLatitude() { return contatoLatitude;}
    public String getContatoLongitude() { return contatoLongitude;}

    public String getAreaconstruida() {
        return areaconstruida;
    }

    public String getAreacapina() {
        return areacapina;
    }

    public String getEnderecolocal() {
        return enderecolocal;
    }
    public String getFrequencia() {
        return frequencia;
    }
    public String getRaio() {
        return raio;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setCodigolocal(String codigolocal) {
        this.codigolocal = codigolocal;
    }
    public void setCentrocusto_id(String centrocusto_id) {
        this.centrocusto_id = centrocusto_id;}
    public void setDescricaolocal(String descricaolocal) {
        this.descricaolocal = descricaolocal;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setLatitude(String latitude) { this.latitude = latitude;}
    public void setLongitude(String longitude) { this.longitude = longitude;}

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setContatoCLID(String contatoCLID) {
        this.contatoCLID = contatoCLID;
    }

    public void setContatoEndereco(String contatoEndereco) {
        this.contatoEndereco = contatoEndereco;
    }

    public void setContatoID(String contatoID) {
        this.contatoID = contatoID;
    }

    public void setContatoLatitude(String contatoLatitude) {
        this.contatoLatitude = contatoLatitude;
    }

    public void setPoloatendimentosID(String poloatendimentosID) {
        this.poloatendimentosID = poloatendimentosID;
    }

    public void setContatoNome(String contatoNome) {
        this.contatoNome = contatoNome;
    }

    public void setPoloatenidmentosDescricao(String poloatenidmentosDescricao) {
        this.poloatenidmentosDescricao = poloatenidmentosDescricao;
    }

    public void setRegiaoDescricao(String regiaoDescricao) {
        this.regiaoDescricao = regiaoDescricao;
    }

    public void setContatoLongitude(String contatoLongitude) {
        this.contatoLongitude = contatoLongitude;
    }

    public void setRegiaoID(String regiaoID) {
        this.regiaoID = regiaoID;
    }

    public void setTempogasto(String tempogasto) {
        this.tempogasto = tempogasto;
    }

    public void setAreaconstruida(String areaconstruida) {
        this.areaconstruida = areaconstruida;
    }

    public void setAreacapina(String areacapina) {
        this.areacapina = areacapina;
    }

    public void setEnderecolocal(String enderecolocal) {
        this.enderecolocal = enderecolocal;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }
    public void setRaio(String raio) {
        this.raio = raio;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
