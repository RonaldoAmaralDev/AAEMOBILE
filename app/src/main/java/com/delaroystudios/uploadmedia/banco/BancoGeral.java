package com.delaroystudios.uploadmedia.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BancoGeral extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "aplusweb300.db";
    public static final String TABELA_LOCAL = "tabela_local2";
    public static final String TABELA_OS = "tabela_os";
    public static final String TABELA_SETOR = "tabela_setor";
    public static final String TABELA_CL = "tabela_centrolucro";
    public static final String TABELA_EQUIPAMENTO = "tabela_equipamento";
    public static final String TABELA_ATIVIDADES = "tabela_atividades";
    public static final String TABELA_LOCALIZACAO = "tabela_localizacao";
    public static final String TABELA_VEICULOS = "tabela_veiculos";
    public static final String TABELA_HH = "tabela_hh";
    public static final String TABELA_TIPOSOLICITACAO = "tabela_tiposolicitao";
    public static final String TABELA_TIPOSERVICO = "tabela_tiposervico";

    public static final String COL_ID_LOCAL = "id";
    public static final String COL_CODIGO_LOCAL = "codigolocal";
    public static final String COL_CENTROCUSTO_LOCAL = "centrocusto_idLocal";
    public static final String COL_DESCRICAO_LOCAL = "descricaolocal";
    public static final String COL_BAIRRO_LOCAL = "bairro";
    public static final String COL_CIDADE_LOCAL = "cidade";
    public static final String COL_LATITUDE_LOCAL = "latitude";
    public static final String COL_LONGITUDE_LOCAL = "longitude";
    public static final String COL_SIGLA_UF = "sigla";
    public static final String COL_ESTADO_UF = "estado";
    public static final String COL_TEMPOGASTO_LOCAL = "tempogasto";
    public static final String COL_REGIAOID_LOCAL = "regiaoID";
    public static final String COL_REGIAODESCRICAO_LOCAL = "regiaoDescricao";
    public static final String COL_POLOATENDIMENTOSID_LOCAL = "poloatendimentosID";
    public static final String COL_POLOATENDIMENTOSDESCRICAO_LOCAL = "poloatendimentosDescricao";
    public static final String COL_CONTATOID_LOCAL = "contatoID";
    public static final String COL_CONTATOCLID_LOCAL = "contatoCLID";
    public static final String COL_CONTATONOME_LOCAL = "contatoNome";
    public static final String COL_CONTATOENDERECO_LOCAL = "contatoEndereco";
    public static final String COL_CONTATOLATITUDE_LOCAL = "contatoLatitude";
    public static final String COL_CONTATOLONGITUDE_LOCAL = "contatoLongitude";
    public static final String COL_AREACONSTRUIDA_LOCAL = "areaconstruida";
    public static final String COL_AREACAPINA_LOCAL = "areacapina";
    public static final String COL_ENDERECO_LOCAL = "enderecolocal";
    public static final String COL_FREQUENCIA_LOCAL = "frequencia";
    public static final String COL_RAIO_LOCAL = "raio";
    public static final String COL_STATUS_LOCAL = "situacao";

    public static final String COL_ID_OS = "id";
    public static final String COL_LOCAL_OS = "local_id";
    public static final String COL_CENTROCUSTO_OS = "centrocusto_idOS";
    public static final String COL_EQUIPAMENTO_OS = "equipamento_id";
    public static final String COL_CHECKLIST_OS = "checklist_id";
    public static final String COL_EQUIPE1_OS = "equipe1";
    public static final String COL_DATAPLANEJAMENTO_OS = "dataplanejamento";
    public static final String COL_DATAPROGRAMACAO_OS = "dataprogramacao";
    public static final String COL_DESCRICAOPADRAO_OS = "descricaopadrao";
    public static final String COL_STATUS_OS = "status";
    public static final String COL_TIPOSOLICITACAOID_OS = "tiposolicitacao_id";
    public static final String COL_TIPOSERVICOID_OS = "tiposervico_id";
    public static final String COL_DATAEXECUCAO_OS = "datexecucao";
    public static final String COL_CODIGOCHAMADO_OS = "codigochamado";
    public static final String COL_FLAG_OS = "flag_os";
    public static final String COL_SITUACAO_OS = "situacao";

    public static final String COL_ID_SETOR = "id";
    public static final String COL_DESCRICAO_SETOR = "descricao";

    public static final String COL_ID_CL = "idCL";
    public static final String COL_CENTROLUCRO_CL = "centrocusto";
    public static final String COL_DESCRICAO_CL = "descricao";

    public static final String COL_ID_EQUIPAMENTO = "idEquipamento";
    public static final String COL_CODIGOEQUIPAMENTO_EQUIPAMENTO = "codigoequipamento";
    public static final String COL_DESCRICAO_EQUIPAMENTO = "descricaoequipamento";
    public static final String COL_CENTROCUSTO_EQUIPAMENTO = "centrocusto_idEquipamento";
    public static final String COL_LOCAL_EQUIPAMENTO = "local_idEquipamento";
    public static final String COL_MODELO_EQUIPAMENTO = "modelo";
    public static final String COL_TAG_EQUIPAMENTO = "tag";
    public static final String COL_NUMEROSERIE_EQUIPAMENTO = "numeroserie";
    public static final String COL_BTU_EQUIPAMENTO = "btu";
    public static final String COL_FABRICANTE_EQUIPAMENTO = "fabricante";
    public static final String COL_TIPOEQUIPAMENTO_EQUIPAMENTO = "tipoequipamento";
    public static final String COL_FORNECEDOR_EQUIPAMENTO = "fornecedor";

    public static final String COL_ID_ITEN = "itenID";
    public static final String COL_CHECKLIST_ITEN = "itenchecklist";
    public static final String COL_DESCRICAO_ITEN = "itenDescricao";
    public static final String COL_STATUS_ITEN = "status";

    public static final String COL_ID_LOCALIZACAO = "id";
    public static final String COL_ID_OBJETO_LOCALIZACAO = "id_objeto";
    public static final String COL_NOME_LOCALIZACAO = "nome_localizacao";
    public static final String COL_LATITUDE_LOCALIZACAO = "latitude_localizacao";
    public static final String COL_LONGITUDE_LOCALIZACAO = "longitude_localizacao";
    public static final String COL_DATAHORA_LOCALIZACAO = "datahora_localizacao";


    public static final String COL_ID_VEICULO = "id";
    public static final String COL_CENTROLUCRO_VEICULO = "centrolucro";
    public static final String COL_COLABORADOR_VEICULO = "colaborador";
    public static final String COL_FABRICANTE_VEICULO = "fabricante";
    public static final String COL_CIDADE_VEICULO = "cidade";
    public static final String COL_ESTADO_VEICULO = "estado";
    public static final String COL_PLACA_VEICULO = "placa";
    public static final String COL_PLACAANTERIOR_VEICULO = "placa_anterior";
    public static final String COL_DESCRICAO_VEICULO = "descricao";
    public static final String COL_MODELO_VEICULO = "modelo";
    public static final String COL_KMINICIAL_VEICULO = "kminicial";
    public static final String COL_ATIVO_VEICULO = "ativo";

    public static final String COL_ID_HH = "id";
    public static final String COL_ORDEMSERVICO_HH = "ordemservico";
    public static final String COL_CENTROCUSTO_HH = "centrocusto_id";
    public static final String COL_HHINICIO_HH = "hhinicio";
    public static final String COL_HHFIM_HH = "hhfim";
    public static final String COL_COLABORADOR_HH = "colaborador_id";


    public static final String COL_ID_TIPOSERVICO = "id";
    public static final String COL_DESCRICAO_TIPOSERVICO = "descricao";

    public static final String COL_ID_TIPOSOLICITACAO = "id";
    public static final String COL_DESCRICAO_TIPOSOLICITACAO = "descricao";

    private SQLiteDatabase databaseGeral;
    private SQLiteOpenHelper openHelper;
    private static final int DATABASE_VERSION = 35;


    public BancoGeral(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase dbGeral) {
        dbGeral.execSQL("create table " +
                TABELA_LOCAL + "" +
                "(id PRIMARY KEY, " +
                "codigolocal TEXT, " +
                "centrocusto_idLocal TEXT, " +
                "descricaolocal TEXT, " +
                "bairro TEXT," +
                "cidade TEXT," +
                "latitude TEXT," +
                "longitude TEXT," +
                "sigla TEXT," +
                "estado TEXT," +
                "tempogasto TEXT," +
                "regiaoID TEXT," +
                "regiaoDescricao TEXT," +
                "poloatendimentosID TEXT," +
                "poloatendimentosDescricao TEXT," +
                "contatoID TEXT," +
                "contatoCLID TEXT," +
                "contatoNome TEXT," +
                "contatoEndereco TEXT," +
                "contatoLatitude TEXT," +
                "contatoLongitude TEXT," +
                "areaconstruida TEXT," +
                "areacapina TEXT," +
                "enderecolocal TEXT, " +
                "frequencia TEXT, " +
                "raio TEXT, " +
                "situacao TEXT)");


        dbGeral.execSQL("create table " +
                TABELA_OS + "" +
                "(id INT PRIMARY KEY, " +
                "local_id TEXT, " +
                "centrocusto_idOS TEXT, " +
                "equipamento_id TEXT, " +
                "checklist_id TEXT, " +
                "equipe1 TEXT," +
                "dataplanejamento TEXT," +
                "dataprogramacao TEXT," +
                "descricaopadrao TEXT," +
                "status TEXT, " +
                "tiposolicitacao_id TEXT, " +
                "tiposervico_id TEXT, " +
                "dataexecucao TEXT, " +
                "codigochamado TEXT, " +
                "flag_os TEXT, " +
                "situacao TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_SETOR + "" +
                "(id PRIMARY KEY, " +
                "descricao TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_CL + "" +
                "(idCL PRIMARY KEY, " +
                "centrocusto TEXT, " +
                "descricao TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_EQUIPAMENTO + "" +
                "(idEquipamento INT PRIMARY KEY," +
                "codigoequipamento TEXT, " +
                "descricaoequipamento TEXT, " +
                "centrocusto_idEquipamento TEXT, " +
                "local_idEquipamento TEXT, " +
                "modelo TEXT," +
                "tag TEXT," +
                "numeroserie TEXT," +
                "btu TEXT," +
                "fabricante TEXT," +
                "tipoequipamento TEXT," +
                "fornecedor TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_ATIVIDADES + "" +
                "(itenID INT PRIMARY KEY , " +
                "itenchecklist TEXT, " +
                "itenDescricao TEXT, " +
                "status TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_LOCALIZACAO + "" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_objeto INT, " +
                "nome_localizacao TEXT, " +
                "latitude_localizacao TEXT, " +
                "longitude_localizacao TEXT, " +
                "datahora_localizacao TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_VEICULOS + "" +
                "(id INTEGER PRIMARY KEY , " +
                "centrolucro TEXT, " +
                "colaborador TEXT, " +
                "fabricante TEXT, " +
                "cidade TEXT, " +
                "estado TEXT, " +
                "placa TEXT, " +
                "placa_anterior TEXT, " +
                "descricao TEXT, " +
                "modelo TEXT, " +
                "kminicial TEXT, " +
                "ativo TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_HH+ "" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ordemservico INT, " +
                "centrocusto_id TEXT, " +
                "hhinicio TEXT, " +
                "hhfim TEXT, " +
                "colaborador_id TEXT)");

        dbGeral.execSQL("create table " +
                TABELA_TIPOSERVICO+ "" +
                "(id INTEGER PRIMARY KEY , " +
                "descricao TEXT) ");

        dbGeral.execSQL("create table " +
                TABELA_TIPOSOLICITACAO+ "" +
                "(id INTEGER PRIMARY KEY , " +
                "descricao TEXT) ");;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_LOCAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_OS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_SETOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_CL);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_EQUIPAMENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ATIVIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_LOCALIZACAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_VEICULOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HH);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_TIPOSERVICO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_TIPOSOLICITACAO);
        onCreate(db);
    }

    public boolean insertTipoServico(String id, String descricao) {
        SQLiteDatabase gravarLocalizacao = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_TIPOSERVICO, id);
        contentValues.put(COL_DESCRICAO_TIPOSERVICO, descricao);
        long result = gravarLocalizacao.insert(TABELA_TIPOSERVICO, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertTipoSolicitacao(String id, String descricao) {
        SQLiteDatabase gravarLocalizacao = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_TIPOSOLICITACAO, id);
        contentValues.put(COL_DESCRICAO_TIPOSOLICITACAO, descricao);
        long result = gravarLocalizacao.insert(TABELA_TIPOSOLICITACAO, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean gravarHHInicio(String ordemservico, String centrocusto_id, String hhinicio,String colaborador_id) {
        SQLiteDatabase gravarLocalizacao = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ORDEMSERVICO_HH, ordemservico);
        contentValues.put(COL_CENTROCUSTO_HH, centrocusto_id);
        contentValues.put(COL_HHINICIO_HH, hhinicio);
        contentValues.put(COL_COLABORADOR_HH, colaborador_id);
        long result = gravarLocalizacao.insert(TABELA_HH, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    /**
     * @param ordemservico;
     * @param hhfim;
     */
    public void updateHHFim(String ordemservico, String hhfim) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_HH + " set hhfim = '" + hhfim + "' where ordemservico =" + "'" + ordemservico + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }


    public boolean gravarLocalizacao(String idObjeto, String latitude, String longitude) {
        SQLiteDatabase gravarLocalizacao = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_OBJETO_LOCALIZACAO, idObjeto);
        contentValues.put(COL_LATITUDE_LOCALIZACAO, latitude);
        contentValues.put(COL_LONGITUDE_LOCALIZACAO, longitude);
        long result = gravarLocalizacao.insert(TABELA_LOCALIZACAO, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor buscaTipoSolicitacao(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSOLICITACAO +
                " WHERE " + COL_ID_TIPOSOLICITACAO + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor buscaTipoServico(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSERVICO +
                " WHERE " + COL_ID_TIPOSERVICO+ " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    //Relatorio Informações TipoSolicitação

    public Cursor getTipoSolicitacao() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSOLICITACAO;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int dbCountTipoSolicitacaoCriadas(String tiposolicitacao_id){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_TIPOSOLICITACAOID_OS+ " = '" + tiposolicitacao_id + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public int dbCountTipoSolicitacaoAbertas(String tiposolicitacao_id){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_TIPOSOLICITACAOID_OS + " = '" + tiposolicitacao_id + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public int dbCountTipoSolicitacaoEncerradas(String tiposolicitacao_id){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_TIPOSOLICITACAOID_OS + " = '" + tiposolicitacao_id + "' AND " + COL_STATUS_OS + " = '" + "sincronizada" + "' OR " + COL_STATUS_OS + " = '" + "encerrada" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public Cursor buscaTipoSolicitacaoOS(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSOLICITACAO +
                " WHERE " + COL_ID_TIPOSOLICITACAO + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    //Relatorio Informações Tipo Serviço

    public Cursor getTipoServico() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSERVICO;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public int dbCountTipoServicoCriadas(String tiposervico_id){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_TIPOSERVICOID_OS+ " = '" + tiposervico_id+ "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public int dbCountEncerradas(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_STATUS_OS + " = '" + "encerrada" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public int dbCountAbertas(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_STATUS_OS + " = '" + "aberta" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public int dbCountEmEspera(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_STATUS_OS + " = '" + "em espera" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }


    public int dbCountTipoServicoAbertas(String tiposolicitacao_id){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_TIPOSERVICOID_OS + " = '" + tiposolicitacao_id + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }

    public int dbCountTipoServicoEncerradas(String tiposolicitacao_id){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_TIPOSERVICOID_OS + " = '" + tiposolicitacao_id + "' AND " + COL_STATUS_OS + " = '" + "sincronizada" + "' OR " + COL_STATUS_OS + " = '" + "encerrada" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }





    /**
     * @param idObjeto;
     * @param latitude;
     * @param longitude;
     */
    public void updateLocalizacao(String idObjeto, String latitude, String longitude) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_LOCALIZACAO + " set latitude_localizacao = '" + latitude + "', longitude = '" + longitude + "' where idObjeto =" + "'" + idObjeto + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public boolean gravarVeiculos(String id, String centrolucro, String colaborador, String fabricante, String cidade, String estado, String placa, String placa_anterior, String descricao, String modelo, String kminicial, String ativo) {
        SQLiteDatabase dbLocal = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_LOCAL, id);
        contentValues.put(COL_CENTROLUCRO_VEICULO, centrolucro);
        contentValues.put(COL_COLABORADOR_VEICULO, colaborador);
        contentValues.put(COL_FABRICANTE_VEICULO, fabricante);
        contentValues.put(COL_CIDADE_VEICULO, cidade);
        contentValues.put(COL_ESTADO_VEICULO, estado);
        contentValues.put(COL_PLACA_VEICULO, placa);
        contentValues.put(COL_PLACAANTERIOR_VEICULO, placa_anterior);
        contentValues.put(COL_DESCRICAO_VEICULO, descricao);
        contentValues.put(COL_MODELO_VEICULO, modelo);
        contentValues.put(COL_KMINICIAL_VEICULO, kminicial);
        contentValues.put(COL_ATIVO_VEICULO, ativo);
        long result = dbLocal.insert(TABELA_VEICULOS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor verificaVeiculo(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_VEICULOS +
                " WHERE " + COL_ID_VEICULO + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaAtividade(String status, String checklist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + COL_STATUS_ITEN + " = '" + status + "' AND " + COL_CHECKLIST_ITEN + " = '" + checklist_id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * @param id;
     * @param centrolucro;
     * @param colaborador;
     * @param fabricante;
     * @param cidade;
     * @param estado;
     * @param placa;
     * @param placa_anterior;
     * @param descricao;
     * @param modelo;
     * @param kminicial
     * @param ativo;
     */
    public void updateVeiculo(String id, String centrolucro, String colaborador, String fabricante, String cidade, String estado, String placa, String placa_anterior, String descricao, String modelo, String kminicial, String ativo) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_VEICULOS + " set " +
                " centrolucro = '" + centrolucro + "'," +
                " colaborador = '" + colaborador + "'," +
                " fabricante = '" + fabricante + "'," +
                " cidade = '" + cidade + "'," +
                " estado = '" + estado + "'," +
                " placa = '" + placa + "'," +
                " placa_anterior = '" + placa_anterior + "'," +
                " descricao = '" + descricao + "'," +
                " modelo = '" + modelo + "'," +
                " kminicial = '" + kminicial + "'," +
                " ativo = '" + ativo + "' WHERE id =" + "'" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public Cursor getVeiculos() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_VEICULOS + "";
        Cursor data = db.rawQuery(query, null);
        data.close();
        return data;
    }


    public String[] buscaLocaisMapa(String search){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + " ( " + COL_DESCRICAO_LOCAL  + " LIKE  '%" +search + "%'" + " OR " + COL_CODIGO_LOCAL  +  "  LIKE  '%" +search + "%'" + " OR " + COL_ENDERECO_LOCAL +  "  LIKE  '%" +search + "%'" + " OR " + COL_BAIRRO_LOCAL  +  "  LIKE  '%" +search + "%'" + " OR " + COL_CIDADE_LOCAL +  "  LIKE  '%" +search + "%'" + " ) " , null);
        cursor.moveToFirst();
        ArrayList<String> text = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            text.add(cursor.getString(cursor.getColumnIndex(COL_CODIGO_LOCAL)));
            text.add(cursor.getString(cursor.getColumnIndex(COL_DESCRICAO_LOCAL)));
            text.add(cursor.getString(cursor.getColumnIndex(COL_ENDERECO_LOCAL)));
            text.add(cursor.getString(cursor.getColumnIndex(COL_BAIRRO_LOCAL)));
            text.add(cursor.getString(cursor.getColumnIndex(COL_CIDADE_LOCAL)));

            cursor.moveToNext();
        }
        cursor.close();
        return text.toArray(new String[text.size()]);
    }


    public Cursor filtroVeiculo(String search){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_VEICULOS +
                " WHERE " + " ( " + COL_PLACA_VEICULO  + " LIKE  '%" +search + "%'" + " OR " + COL_DESCRICAO_VEICULO +  "  LIKE  '%" +search + "%'" + " OR " + COL_MODELO_VEICULO + "  LIKE  '%" +search + "%'" + " ) ";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public boolean insertLocal(String id, String codigolocal, String centrocusto_idLocal,
                               String descricaolocal, String bairro, String cidade, String latitude,
                               String longitude, String sigla, String estado, String tempogasto, String regiaoID,
                               String regiaoDescricao, String poloatendimentosID, String poloatendimentosDescricao, String contatoID,
                               String contatoCLID, String contatoNome, String contatoEndereco, String contatoLatitude,
                               String contatoLongitude, String areaconstruida, String areacapina, String enderecolocal, String frequencia, String raio, String situacao) {
        SQLiteDatabase dbLocal = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_LOCAL, id);
        contentValues.put(COL_CODIGO_LOCAL, codigolocal);
        contentValues.put(COL_CENTROCUSTO_LOCAL, centrocusto_idLocal);
        contentValues.put(COL_DESCRICAO_LOCAL, descricaolocal);
        contentValues.put(COL_BAIRRO_LOCAL, bairro);
        contentValues.put(COL_CIDADE_LOCAL, cidade);
        contentValues.put(COL_LATITUDE_LOCAL, latitude);
        contentValues.put(COL_LONGITUDE_LOCAL, longitude);
        contentValues.put(COL_SIGLA_UF, sigla);
        contentValues.put(COL_ESTADO_UF, estado);
        contentValues.put(COL_TEMPOGASTO_LOCAL, tempogasto);
        contentValues.put(COL_REGIAOID_LOCAL, regiaoID);
        contentValues.put(COL_REGIAODESCRICAO_LOCAL, regiaoDescricao);
        contentValues.put(COL_POLOATENDIMENTOSID_LOCAL, poloatendimentosID);
        contentValues.put(COL_POLOATENDIMENTOSDESCRICAO_LOCAL, poloatendimentosDescricao);
        contentValues.put(COL_CONTATOID_LOCAL, contatoID);
        contentValues.put(COL_CONTATOCLID_LOCAL, contatoCLID);
        contentValues.put(COL_CONTATONOME_LOCAL, contatoNome);
        contentValues.put(COL_CONTATOENDERECO_LOCAL, contatoEndereco);
        contentValues.put(COL_CONTATOLATITUDE_LOCAL, contatoLatitude);
        contentValues.put(COL_CONTATOLONGITUDE_LOCAL, contatoLongitude);
        contentValues.put(COL_AREACONSTRUIDA_LOCAL, areaconstruida);
        contentValues.put(COL_AREACAPINA_LOCAL, areacapina);
        contentValues.put(COL_ENDERECO_LOCAL, enderecolocal);
        contentValues.put(COL_FREQUENCIA_LOCAL, frequencia);
        contentValues.put(COL_RAIO_LOCAL, raio);
        contentValues.put(COL_STATUS_LOCAL, situacao);
        long result = dbLocal.insert(TABELA_LOCAL, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertSetor(String id, String descricao) {
        SQLiteDatabase dbLocal = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_SETOR, id);
        contentValues.put(COL_DESCRICAO_SETOR, descricao);
        long result = dbLocal.insert(TABELA_SETOR, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertCentroCusto(String idCL, String centrocusto, String descricao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_CL, idCL);
        contentValues.put(COL_CENTROLUCRO_CL, centrocusto);
        contentValues.put(COL_DESCRICAO_CL, descricao);
        long result = db.insert(TABELA_CL, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertAtividades(String itenID, String itenchecklist, String itenDescricao, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_ITEN, itenID);
        contentValues.put(COL_CHECKLIST_ITEN, itenchecklist);
        contentValues.put(COL_DESCRICAO_ITEN, itenDescricao);
        contentValues.put(COL_STATUS_ITEN, status);
        long result = db.insert(TABELA_ATIVIDADES, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public void updateAtividades(String itenID, String itenchecklist, String itenDescricao) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_ATIVIDADES + " set itenchecklist = '" + itenchecklist + "', itenDescricao = '" + itenDescricao + "' where itenID =" + "'" + itenID + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public void updateTipoSolicitacao(String id, String descricao) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_TIPOSOLICITACAO + " set descricao = '" + descricao + "' where id =" + "'" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }


    public void updateTipoServico(String id, String descricao) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_TIPOSERVICO + " set descricao = '" + descricao + "' where id =" + "'" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public void updateStatusAtividadeAberta(String checklist_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_ATIVIDADES + " set status = '" + "aberta" + "' where " +  COL_CHECKLIST_ITEN + " = '" + checklist_id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }


    public void updateStatusAtividade(String idAtividade, String checklist_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_ATIVIDADES + " set status = '" + "encerrada" + "' where " + COL_ID_ITEN + " =" + "'" + idAtividade + "' AND " + COL_CHECKLIST_ITEN + " = '" + checklist_id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public void updateCL(String idCL, String centrocusto, String descricao) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_CL + " set centrocusto = '" + centrocusto + "', descricao = '" + descricao + "' where idCL =" + "'" + idCL + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public Cursor verificaCL(String idCL) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_CL +
                " WHERE " + COL_ID_CL + " = '" + idCL + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor buscaOSContrato(String contrato_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_CENTROCUSTO_OS + " = '" + contrato_id + "'";
        Cursor data = db.rawQuery(query, null);
        Log.d("query",query);
      //  db.close();
        return data;
    }


    public Cursor verificaOSEquipamento(String equipamento_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamento_id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor buscaAtividades(String idAtividade, String checklist_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + COL_ID_ITEN + " = '" + idAtividade + "' AND " + COL_CHECKLIST_ITEN + " = '" + checklist_ID + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor verificaAtividadesJson(String idAtividade) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + COL_CHECKLIST_ITEN + " = '" + idAtividade+ "' ORDER BY "+ COL_ID_ITEN + " ASC LIMIT 1" ;
        Cursor data = db.rawQuery(query, null);
        return data;

    }

    public Cursor verificaAtividades(String idAtividade) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + COL_CHECKLIST_ITEN + " = '" + idAtividade+ "'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getDataCL() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_CL + " ORDER BY " + COL_CENTROLUCRO_CL;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    /**
     * Update Sync status against each User ID
     *
     * @param idEquipamento;
     * @param codigoequipamento;
     * @param descricaoequipamento;
     * @param centrocusto_idEquipamento;
     * @param local_idEquipamento;
     * @param modelo;
     * @param tag;
     * @param numeroserie;
     * @param btu;
     * @param fabricante;
     * @param tipoequipamento;
     * @param fornecedor;
     */
    public void updateEquipamento(String idEquipamento, String codigoequipamento, String descricaoequipamento, String centrocusto_idEquipamento, String local_idEquipamento, String modelo, String tag, String numeroserie, String btu, String fabricante, String tipoequipamento, String fornecedor) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_EQUIPAMENTO + " set codigoequipamento = '" + codigoequipamento + "', descricaoequipamento = '" + descricaoequipamento + "', centrocusto_idEquipamento = '" + centrocusto_idEquipamento + "', local_idEquipamento = '" + local_idEquipamento +
                "', modelo = '" + modelo + "', tag = '" + tag + "', numeroserie = '" + numeroserie + "', btu = '" + btu +  "', fabricante = '" + fabricante  +  "', tipoequipamento = '" + tipoequipamento +  "', fornecedor = '" + fornecedor + "' where idEquipamento=" + "'" + idEquipamento + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    public boolean insertEquipamento(String idEquipamento, String codigoequipamento, String descricaoequipamento, String centrocusto_idEquipamento, String local_idEquipamento, String fabricante, String tipoequipamento, String fornecedor) {
        SQLiteDatabase dbLocal = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_EQUIPAMENTO, idEquipamento);
        contentValues.put(COL_CODIGOEQUIPAMENTO_EQUIPAMENTO, codigoequipamento);
        contentValues.put(COL_DESCRICAO_EQUIPAMENTO, descricaoequipamento);
        contentValues.put(COL_CENTROCUSTO_EQUIPAMENTO, centrocusto_idEquipamento);
        contentValues.put(COL_LOCAL_EQUIPAMENTO, local_idEquipamento);
        contentValues.put(COL_FABRICANTE_EQUIPAMENTO, fabricante);
        contentValues.put(COL_TIPOEQUIPAMENTO_EQUIPAMENTO, tipoequipamento);
        contentValues.put(COL_FORNECEDOR_EQUIPAMENTO, fornecedor);

        long result = dbLocal.insert(TABELA_EQUIPAMENTO, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getTag(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO +
                " WHERE " + COL_TAG_EQUIPAMENTO + " = '" + tag + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaVisita(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public List<String> getLocals(String id){
        List<String> labels = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABELA_LOCAL  + " WHERE " + COL_CENTROCUSTO_LOCAL + " = '" + id + "'" + "ORDER BY " + COL_CODIGO_LOCAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }

    public Cursor verificaLocalsSpinner(String local){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_DESCRICAO_LOCAL + " = '" + local +  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor verificaTipoSolicitacao(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSOLICITACAO +
                " WHERE " + COL_ID_TIPOSOLICITACAO + " = '" + id +  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor verificaTipoServico(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_TIPOSERVICO +
                " WHERE " + COL_ID_TIPOSERVICO + " = '" + id +  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDataEquipamento(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDataEquipamentoOrdem(String local_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO +
                " WHERE " + COL_LOCAL_EQUIPAMENTO + " = '" + local_id + "'" + "ORDER BY " + COL_CODIGOEQUIPAMENTO_EQUIPAMENTO;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaAtividadesAbertas(String checklist_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + COL_CHECKLIST_ITEN + " = '" + checklist_id + "'" + " AND " + COL_STATUS_ITEN + " = '" + "aberta" +  "' ORDER BY " + COL_ID_ITEN;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor verificaEquipamento(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO +
                " WHERE " + COL_ID_EQUIPAMENTO + " = '" + id+  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor verificaEquipamentoTAG(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO +
                " WHERE " + COL_TAG_EQUIPAMENTO + " = '" + tag+  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public int dbCountItem(String checklist_id){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + COL_CHECKLIST_ITEN + " = '" + checklist_id +  "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountLocais(){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_LOCAL;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountAtividadesAbertas(String checklist_id){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_ATIVIDADES + " WHERE " + COL_CHECKLIST_ITEN + " = '" + checklist_id + "' AND " + COL_STATUS_ITEN + " = '" + "aberta" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }



    public int dbCountLocaisCL(String contrato_id){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_LOCAL + " WHERE " + COL_CENTROCUSTO_LOCAL + " = '" + contrato_id + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }


    public int dbCountEquipamento(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_EQUIPAMENTO;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountEquipamentoCL(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_EQUIPAMENTO + " WHERE " + COL_CENTROCUSTO_EQUIPAMENTO + " = '" + equipamentoID + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountPrevCriadas(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamentoID + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "1" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountPrevExecutadas(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamentoID + "' AND " + COL_STATUS_OS + " = '" + "encerrada" + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "1" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountPrevPendentes(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamentoID + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "1" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountCorretivaCriadas(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamentoID + "' AND " + COL_TIPOSOLICITACAOID_OS+ " = '" + "2" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountCorretivaExecutadas(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamentoID + "' AND " + COL_STATUS_OS + " = '" + "encerrada" + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "2" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }

    public int dbCountCorretivaPendentes(String equipamentoID){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamentoID + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "2" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }


    /**
     * Update Tabela Local
     * @param id;
     * @param codigolocal;
     * @param centrocusto_idLocal;
     * @param descricaolocal;
     * @param bairro;
     * @param cidade;
     * @param latitude;
     * @param longitude;
     * @param sigla;
     * @param estado;
     * @param tempogasto;
     * @param regiaoID;
     * @param regiaoDescricao;
     * @param poloatendimentosID;
     * @param poloatendimentosDescricao;
     * @param contatoID;
     * @param contatoCLID;
     * @param contatoNome;
     * @param contatoEndereco;
     * @param contatoLatitude;
     * @param contatoLongitude;
     * @param enderecolocal;
     */
    public void updateLocal(String id, String codigolocal, String centrocusto_idLocal, String descricaolocal, String bairro, String cidade,
                            String latitude, String longitude, String sigla,
                            String estado, String tempogasto, String regiaoID, String regiaoDescricao, String poloatendimentosID,
                            String poloatendimentosDescricao, String contatoID, String contatoCLID, String contatoNome, String contatoEndereco,
                            String contatoLatitude, String contatoLongitude, String areaconstruida, String areacapina, String enderecolocal){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_LOCAL + " set codigolocal = '"+ codigolocal + "', " +
                "centrocusto_idLocal = '"+ centrocusto_idLocal + "', descricaolocal = '"+ descricaolocal  + "', bairro = '"+ bairro  +"', cidade = '"+ cidade  +"', latitude = '"+ latitude + "', longitude = '"+ longitude  +"', sigla = '"+ sigla  +"', estado = '"+ estado +"', tempogasto = '"+ tempogasto +"' , regiaoID = '"+ regiaoID +"', regiaoDescricao = '"+ regiaoDescricao+"', poloatendimentosID = '"+ poloatendimentosID +"', poloatendimentosDescricao = '"+ poloatendimentosDescricao +"', contatoID = '"+ contatoID +"' , contatoCLID = '"+ contatoCLID +"' , contatoNome = '"+ contatoNome +"', contatoEndereco = '"+ contatoEndereco +"', contatoLatitude = '"+ contatoLatitude +"', contatoLongitude= '"+ contatoLongitude + "', areaconstruida= '"+ areaconstruida +"', areacapina= '"+ areacapina + "', enderecolocal = '"+ enderecolocal +"' WHERE id="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }


    public Cursor buscaLocal(String centrocusto_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_CENTROCUSTO_LOCAL + " = '" + centrocusto_id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaLocalFrota(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaEquipamentos(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO + "";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaOS(String equipamento_id, String colaborador_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamento_id  + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "' AND " + COL_EQUIPE1_OS + " = '" + colaborador_id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor buscaOSColaborador(String colaborador_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPE1_OS + " = '" + colaborador_id + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor qrCode(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_ID_LOCAL + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor filtro(String search, String centrocusto_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + " ( " + COL_DESCRICAO_LOCAL  + " LIKE  '%" +search + "%'" + " OR " + COL_CODIGO_LOCAL  +  "  LIKE  '%" +search + "%'" + " OR " + COL_ENDERECO_LOCAL +  "  LIKE  '%" +search + "%'" + " OR " + COL_BAIRRO_LOCAL  +  "  LIKE  '%" +search + "%'" + " OR " + COL_CIDADE_LOCAL +  "  LIKE  '%" +search + "%'" + " ) " +  " AND " + COL_CENTROCUSTO_LOCAL + " = '" + centrocusto_id+  "'";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;

    }

    public Cursor buscaVisitasAbertas(String search){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE "  + COL_STATUS_OS + " = '" + search +  "'";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor buscaVisitasEmEspera(String search){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE "  + COL_STATUS_OS + " = '" + search +  "'";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor buscaVisitasSincronizadas(String search){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE "  + COL_STATUS_OS + " = '" + search +  "'";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor filtroOS(String search, String colaborador_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + " ( " + COL_ID_OS  + " LIKE  '%" +search + "%'" + " OR " + COL_DATAPLANEJAMENTO_OS  +  "  LIKE  '%" +search + "%'"  + " ) " +  " AND " + COL_EQUIPE1_OS + " = '" + colaborador_id +  "'";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor filtroAtividade(String search, String centrocusto_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_ATIVIDADES +
                " WHERE " + " ( " + COL_DESCRICAO_ITEN  + " LIKE  '%" +search + "%'" + " ) " +  " AND " + COL_CHECKLIST_ITEN + " = '" + centrocusto_id +  "'";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }


    public Cursor filtroEquipamentos(String search){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_EQUIPAMENTO +
                " WHERE " + " ( " + COL_CODIGOEQUIPAMENTO_EQUIPAMENTO  + " LIKE  '%" +search + "%'" + " OR " + COL_DESCRICAO_EQUIPAMENTO  +  "  LIKE  '%" + search + "%'" +  " OR " + COL_MODELO_EQUIPAMENTO  +  "  LIKE  '%" +search + "%'"  + " OR " + COL_NUMEROSERIE_EQUIPAMENTO  +  "  LIKE  '%" +search + "%'"  + " OR " + COL_DESCRICAO_EQUIPAMENTO  +  "  LIKE  '%" + search + "%'" +  " OR " + COL_MODELO_EQUIPAMENTO  +  "  LIKE  '%" +search + "%'"  + " OR " + COL_FABRICANTE_EQUIPAMENTO  +  "  LIKE  '%" +search + "%'" +  " OR " + COL_BTU_EQUIPAMENTO  +  "  LIKE  '%" + search + "%'" +  " OR " + COL_MODELO_EQUIPAMENTO  +  "  LIKE  '%" +search + "%'"  + " OR " + COL_NUMEROSERIE_EQUIPAMENTO  +  "  LIKE  '%" +search + "%'" + ") ";
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;


    }
    public Cursor getDataLocal(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscarColaboradores(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL + " WHERE " + COL_CONTATONOME_LOCAL;
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    public String[] SelectAllData() {
        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String query = "SELECT * FROM " + TABELA_OS;
            Cursor data = db.rawQuery(query, null);
            if(data != null)
            {
                if (data.moveToFirst()) {
                    arrData = new String[data.getCount()];
                    int i= 0;
                    do {
                        arrData[i] = data.getString(1);
                        i++;
                    } while (data.moveToNext());
                }
            }
            data.close();
            return arrData;
        } catch (Exception e) {
            return null;
        }
    }


    public List<String> getAllLabels(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABELA_CL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public Cursor buscaLocais(String centrocusto_idLocal, String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " INNER JOIN  " + TABELA_OS +  " ON " + COL_LOCAL_OS + " = " + COL_ID_LOCAL + " WHERE " + COL_CENTROCUSTO_LOCAL + " = '" + centrocusto_idLocal + "'" + " AND " + COL_EQUIPE1_OS + " = '" + id +  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor verificaLocal(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_ID_LOCAL + " = '" + id+  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }




    public Cursor getDataCL(String centrocusto_idLocal){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_DESCRICAO_LOCAL + " = '" + centrocusto_idLocal + "'" + "ORDER BY " + COL_CODIGO_LOCAL;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaContrato(String centrolucro_descricao){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_CL  + " WHERE " + COL_DESCRICAO_CL+ " = '" + centrolucro_descricao + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemIDLocal(String codigolocal){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_ID_LOCAL + " FROM " + TABELA_LOCAL +
                " WHERE " + COL_CODIGO_LOCAL + " = '" + codigolocal + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int dbSyncCountLocal(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_LOCAL  + "";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        if(cursor != null) {
            cursor.close();
        }
        database.close();
        return count;
    }


    public boolean insertOS(String id, String local_id, String centrocusto_idOS, String tiposolicitacao_id, String tiposervico_id, String equipamento_id,  String checklist_id, String equipe1, String dataplanejamento, String descricaopadrao, String status, String codigochamado, String flag_os) {
        SQLiteDatabase dbLocal = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_OS, id);
        contentValues.put(COL_LOCAL_OS, local_id);
        contentValues.put(COL_CENTROCUSTO_OS, centrocusto_idOS);
        contentValues.put(COL_TIPOSOLICITACAOID_OS, tiposolicitacao_id);
        contentValues.put(COL_TIPOSERVICOID_OS, tiposervico_id);
        contentValues.put(COL_EQUIPAMENTO_OS, equipamento_id);
        contentValues.put(COL_CHECKLIST_OS, checklist_id);
        contentValues.put(COL_EQUIPE1_OS, equipe1);
        contentValues.put(COL_DATAPLANEJAMENTO_OS, dataplanejamento);
        contentValues.put(COL_DESCRICAOPADRAO_OS, descricaopadrao);
        contentValues.put(COL_STATUS_OS, status);
        contentValues.put(COL_CODIGOCHAMADO_OS, codigochamado);
        contentValues.put(COL_FLAG_OS, flag_os);
        long result = dbLocal.insert(TABELA_OS,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }



    /**
     * Update Sync status against each User ID
     * @param id;
     * @param centrocusto_idOS;
     * @param equipamento_id;
     * @param checklist_id;
     * @param equipe1;
     * @param dataplanejamento;
     * @param descricaopadrao;
     * @param tiposervico_id;
     * @param tiposervico_descricao;
     * @param flag_os;
     */

    public void updateOS(String id, String local_id, String centrocusto_idOS, String tiposervico_id, String tiposervico_descricao, String equipamento_id, String checklist_id, String equipe1, String dataplanejamento, String descricaopadrao, String flag_os){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_OS + " set local_id= '" + local_id + "', centrocusto_idOS= '" + centrocusto_idOS + "',    equipamento_id = '"+ equipamento_id + "', checklist_id = '" + checklist_id + "', equipe1= '"+ equipe1  +"', dataplanejamento = '" + dataplanejamento + "', descricaopadrao = '"+ descricaopadrao  + "', tiposervico_id = '"+ tiposervico_id  + "', tiposervico_descricao = '"+ tiposervico_descricao +  "', flag_os = '"+ flag_os +"' where id="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }




    public int dbCoutaberta(String idColaborador){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPE1_OS + " = '" + idColaborador + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }


    public ArrayList<HashMap<String, String>> getOrdemServicos() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM  " + TABELA_OS;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ordemservico", cursor.getString(0));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        Log.d("query",selectQuery);
        database.close();
        return wordList;
    }

    public String gravarNovaOS2(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_STATUS_OS + " = '" + "sincronizada"  + "' AND " + COL_SITUACAO_OS + " = '" + "A" +"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(wordList);
    }


    public void updateSituacaoOSSistematica(String id, String situacao){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_OS + " set situacao= '" + situacao  +"' WHERE id="+"'"+ id + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "1" + "' AND " + COL_SITUACAO_OS + " != '" + "OK" +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }


    //Apos Gerar Nova OS ele ira mudar status para não ter duplicidade
    public void updateSituacaoOrdemServico(String os_id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_OS + " set situacao = '"+ status + "' where id="+"'"+ os_id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }

    public int dbCountNovasVisitas(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS + " WHERE " + COL_SITUACAO_OS + " = '" + "A" + "' AND " + COL_STATUS_OS + " = '" + "sincronizada" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        Log.d("query",selectQuery);
        database.close();
        return count;
    }



    public int dbCoutencerrada(String idColaborador){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPE1_OS + " = '" + idColaborador + "' AND " + COL_STATUS_OS + " = '" + "encerrada" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }



    public int dbCoutsync(String idColaborador){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPE1_OS + " = '" + idColaborador + "' AND " + COL_STATUS_OS + " = '" + "sincronizada" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }


    /**
     * Update Sync status against each User ID
     * @param ordemservico_id;
     */

    public void updateSyncStatus(String ordemservico_id){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_OS + " set status = 'sincronizada" + "', situacao =  'A' where id="+"'"+ ordemservico_id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }


    /**
     * Update Sync status against each User ID
     * @param os_id;
     */
    public void updateModoEspera(String os_id){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_OS + " set status = 'em espera" +"' where id="+"'"+ os_id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }


    /**
     * Update Sync status against each User ID
     * @param os_id;
     */
    public void updateEncerradaStatus(String os_id){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_OS + " set status = 'encerrada" +"' where id="+"'"+ os_id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
    }


    public Cursor OSAberta(String equipamento_id, String idColaborador){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamento_id + "' AND " + COL_EQUIPE1_OS + " = '" + idColaborador + "' AND " + COL_STATUS_OS + " = '" + "aberta" + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM  " + TABELA_OS ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("centrocusto", cursor.getString(1));
                map.put("descricao", cursor.getString(2));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }


    public ArrayList<HashMap<String, String>> buscaOSPContrato(String id) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM  " + TABELA_OS + " WHERE " + COL_CENTROCUSTO_OS + " = '" + id +  "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public Cursor buscaOSInspecao(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM  " + TABELA_OS + " WHERE " + COL_CENTROCUSTO_OS + " = '" + id + "' AND " + COL_TIPOSOLICITACAOID_OS + " = '" + "6" + "' AND " + COL_STATUS_OS + " != '" + "aberta" +"'";
        Cursor data = db.rawQuery(selectQuery, null);
        Log.d("query",selectQuery);
        return data;
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDataCL(String equipamento_id, String idColaborador){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_EQUIPAMENTO_OS + " = '" + equipamento_id + "' AND " + COL_EQUIPE1_OS + " = '" + idColaborador + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor verificaOS(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_ID_OS + " = '" + id +  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor verificaSetor(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_SETOR +
                " WHERE " + COL_ID_SETOR + " = '" + id +  "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getItemID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OS +
                " WHERE " + COL_ID_OS + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_OS  + "";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        if(cursor != null) {
            cursor.close();
        }
        return count;
    }



    public Cursor buscaDadosColaborador(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_CONTATOID_LOCAL + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public int dbCountEstacoesColaborador(String id){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABELA_LOCAL +
                " WHERE " + COL_CONTATOID_LOCAL + " = '" + id +  "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        return count;
    }


    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABELA_LOCAL, "ID = ?",new String[] {id});
    }

    public void open() {

        this.databaseGeral = openHelper.getWritableDatabase();
    }

    public void deleteDados()
    {
        SQLiteDatabase db = getWritableDatabase();
        String sqlLocal = "DELETE FROM " + TABELA_LOCAL;
        String sqlOS= "DELETE FROM " + TABELA_OS;
        String sqlSetor = "DELETE FROM " + TABELA_SETOR;
        String sqlCL = "DELETE FROM " + TABELA_CL;
        String sqlEquipamento = "DELETE FROM " + TABELA_EQUIPAMENTO;
        String sqlAtividades = "DELETE FROM " + TABELA_ATIVIDADES;
        String sqlLocalizacao = "DELETE FROM " + TABELA_LOCALIZACAO;
        String sqlVeiculos= "DELETE FROM " + TABELA_VEICULOS;

        db.execSQL(sqlLocal);
        db.execSQL(sqlOS);
        db.execSQL(sqlSetor);
        db.execSQL(sqlCL);
        db.execSQL(sqlEquipamento);
        db.execSQL(sqlAtividades);
        db.execSQL(sqlLocalizacao);
        db.execSQL(sqlVeiculos);

    }


    /**
     * Close the database connection.
     */
    public void close() {
        if (databaseGeral != null) {
            this.databaseGeral.close();
        }
    }

}