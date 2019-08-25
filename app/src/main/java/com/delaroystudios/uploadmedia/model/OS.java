package com.delaroystudios.uploadmedia.model;



import java.io.Serializable;

public class OS implements Serializable {

    private String id;
    private String local_id;
    private String centrocusto_id;
    private String tiposolicitacao;
    private String tiposervico;
    private String equipamento_id;
    private String checklist_id;
    private String equipe1;
    private String dataplanejamento;
    private String dataprogramacao;
    private String descricaopadrao;
    private String codigochamado;
    private String flag_os;


    public OS() {

    }


    public OS(String id, String local_id, String centrocusto_id, String tiposolicitacao, String tiposervico, String equipamento_id, String checklist_id, String equipe1, String dataplanejamento, String dataprogramacao, String descricaopadrao, String codigochamado, String flag_os) {
        this.id = id;
        this.local_id = local_id;
        this.centrocusto_id = centrocusto_id;
        this.tiposolicitacao= tiposolicitacao;
        this.tiposervico = tiposervico;
        this.equipamento_id = equipamento_id;;
        this.checklist_id = checklist_id;
        this.equipe1 =  equipe1;
        this.dataplanejamento = dataplanejamento;
        this.dataprogramacao = dataprogramacao;
        this.descricaopadrao = descricaopadrao;
        this.codigochamado = codigochamado;
        this.flag_os = flag_os;

    }

    public String getId() { return  id;}

    public String getLocal_id() {
        return local_id;
    }

    public String getCentrocusto_id() { return centrocusto_id; }

    public String getTiposolicitacao() { return tiposolicitacao;}

    public String getTiposervico() {
        return tiposervico;
    }

    public String getEquipamento_id() {
        return equipamento_id;
    }

    public String getChecklist_id () { return checklist_id; }

    public String getEquipe1() {
        return equipe1;
    }

    public String getDataplanejamento() { return dataplanejamento;}

    public String getDataprogramacao() { return  dataprogramacao;}

    public String getDescricaopadrao() { return descricaopadrao;}

    public String getCodigochamado() {
        return codigochamado;
    }

    public String getFlag_os() {
        return flag_os;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }
    public void setCentrocusto_id(String centrocusto_id) { this.centrocusto_id = centrocusto_id;}
    public void setTiposolicitacao(String tiposolicitacao) { this.tiposolicitacao = tiposolicitacao;}

    public void setTiposervico(String tiposervico) {
        this.tiposervico = tiposervico;
    }

    public void setEquipamento_id(String equipamento_id) {
        this.equipamento_id = equipamento_id;
    }
    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }
    public void setEquipe1(String equipe1) { this.equipe1 = equipe1; }
    public void setDataplanejamento(String dataplanejamento) { this.dataplanejamento = dataplanejamento; }
    public void setDataprogramacao(String dataprogramacao) { this.dataprogramacao = dataprogramacao; }
    public void setDescricaopadrao(String descricaopadrao) { this.descricaopadrao = descricaopadrao; }

    public void setCodigochamado(String codigochamado) {
        this.codigochamado = codigochamado;
    }

    public void setFlag_os(String flag_os) {
        this.flag_os = flag_os;
    }
}


