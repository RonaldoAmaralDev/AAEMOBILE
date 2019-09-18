package br.com.araujoabreu.timg.banco;

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

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "aplusweb254.db";
    public static final String TABLE_NAME = "tabela_teste11";
    public static final String TABELA_MEDICAO = "tabela_medicao";

    public static final String COL_0 = "id";
    public static final String COL_1 = "ordemservico_id";
    public static final String COL_2 = "checklist_id";
    public static final String COL_3 = "checklistitens_id";
    public static final String COL_4 = "foto1a";
    public static final String COL_5 = "foto2a";
    public static final String COL_6 = "foto3a";
    public static final String COL_7 = "foto1d";
    public static final String COL_8 = "foto2d";
    public static final String COL_9 = "foto3d";
    public static final String COL_10 = "inicio";
    public static final String COL_11 = "fim";
    public static final String COL_12 = "observacaoantes";
    public static final String COL_13 = "observacaodepois";
    public static final String COL_14 = "latitude";
    public static final String COL_15 = "longitude";
    public static final String COL_16 = "situacao";
    public static final String COL_17 = "medida1";
    public static final String COL_18 = "medida2";
    public static final String COL_23 = "dataencerramento";
    public static final String COL_24 = "assinaturaColaborador";
    public static final String COL_25 = "assinaturaCliente";
    public static final String COL_26 = "update_Status";


    public static final String COL_ID_TABELAMEDICAO = "id";
    public static final String COL_OS_TABELAMEDICAO = "ordemservico";
    public static final String COL_MEDICAOAGUA1_TABELAMEDICAO = "medicaoagua1";
    public static final String COL_MEDICAOAGUA2_TABELAMEDICAO = "medicaoagua2";
    public static final String COL_MEDICAOLUZ1 = "medicaoluz1";
    public static final String COL_MEDICAOLUZ2 = "medicaoluz2";
    public static final String COL_CL_TABELAMEDICAO = "centrolucro";
    public static final String COL_STATUS_TABELAMEDICAO = "status";
    public static final String COL_DATA_TABELAMEDICAO = "data";


    private static final int DATABASE_VERSION = 19;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, ordemservico_id TEXT, " +
                "checklist_id TEXT, checklistitens_id TEXT,  foto1a TEXT, "+
                " foto2a TEXT, foto3a TEXT, foto1d TEXT, foto2d TEXT, foto3d TEXT, inicio TEXT," +
                " fim TEXT, observacaoantes TEXT, observacaodepois TEXT, latitude TEXT, longitude TEXT, situacao TEXT, " +
                "medida1 TEXT, medida2 TEXT, leitura_agua1 TEXT, leitura_agua2 TEXT, leitura_energia1 TEXT, leitura_energia2 TEXT, dataencerramento TEXT, assinaturaColaborador TEXT, assinaturaCliente TEXT, " +
                "update_Status TEXT, centrocusto_id TEXT)");


        db.execSQL("create table " +
                TABELA_MEDICAO + "" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ordemservico TEXT, " +
                "medicaoagua1 TEXT, " +
                "medicaoagua2 TEXT, " +
                "medicaoluz1 TEXT, " +
                "medicaoluz2 TEXT, " +
                "centrolucro TEXT, " +
                "status TEXT, " +
                "data TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }




    /**
     * Update Sync status against each User ID
     * @param checklist_id;
     * @param checklistitens_id;
     * @param inicio;
     * @param observacaoantes;
     * @param latitude;
     * @param longitude;
     * @param situacao;
     * @param update_Status;
     */


    public void updateAtividade(String os, String checklist_id, String checklistitens_id, String inicio, String observacaoantes, String latitude, String longitude, String situacao, String update_Status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set checklist_id = '" + checklist_id + "', checklistitens_id = '" + checklistitens_id + "', inicio = '" + inicio+ "', observacaoantes = '" + observacaoantes + "', latitude = '" + latitude  + "', longitude = '" + longitude +"', situacao = '" + situacao +"', update_Status = '" + update_Status +"' where ordemservico_id ="+"'"+ os + "' AND " + COL_3 + " = '" + checklistitens_id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    public boolean insertMedicao(String ordemservico, String medicaoagua1, String medicaoagua2, String medicaoluz1, String medicaoluz2, String centrolucro, String status, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_OS_TABELAMEDICAO, ordemservico );
        contentValues.put(COL_MEDICAOAGUA1_TABELAMEDICAO, medicaoagua1);
        contentValues.put(COL_MEDICAOAGUA2_TABELAMEDICAO, medicaoagua2);
        contentValues.put(COL_MEDICAOLUZ1, medicaoluz1);
        contentValues.put(COL_MEDICAOLUZ2, medicaoluz2);
        contentValues.put(COL_CL_TABELAMEDICAO, centrolucro);
        contentValues.put(COL_STATUS_TABELAMEDICAO, status);
        contentValues.put(COL_DATA_TABELAMEDICAO, data);
        long result = db.insert(TABELA_MEDICAO,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }



    public boolean insertFoto1(String ordemservico, String checklist_id, String checklistitens_id, String foto1a, String updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, ordemservico );
        contentValues.put(COL_2, checklist_id);
        contentValues.put(COL_3, checklistitens_id);
        contentValues.put(COL_4, foto1a);
        contentValues.put(COL_26, updateStatus);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public Cursor pegarAtividades(String idAtividade, String checklist_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_3 + " = '" + idAtividade + "' AND " + COL_2 + " = '" + checklist_ID + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getAllData() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getVisitas(String visita) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_1 + " = '" + visita + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaMedicoes() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_MEDICAO;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor buscaDados(String ordemservico) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_1 + " = '" + ordemservico;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM  " + TABLE_NAME ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ordemservico", cursor.getString(1));
                map.put("checklist", cursor.getString(2));
                map.put("checklistitens", cursor.getString(3));
                map.put("foto1a", cursor.getString(4));
                map.put("foto2a", cursor.getString(5));
                map.put("foto3a", cursor.getString(6));
                map.put("foto1d", cursor.getString(7));
                map.put("foto2d", cursor.getString(8));
                map.put("foto3d", cursor.getString(9));
                map.put("inicio", cursor.getString(10));
                map.put("fim", cursor.getString(11));
                map.put("observacaoantes", cursor.getString(12));
                map.put("observacaodepois", cursor.getString(13));
                map.put("latitude", cursor.getString(14));
                map.put("longitude", cursor.getString(15));
                map.put("situacao", cursor.getString(16));
                map.put("dataencerramento", cursor.getString(23));
                map.put("assinaturaColaborador", cursor.getString(24));
                map.put("assinaturaCliente", cursor.getString(25));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }



    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME  +" where update_Status = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ordemservico", cursor.getString(1));
                map.put("checklist", cursor.getString(2));
                map.put("checklistitens", cursor.getString(3));
                map.put("foto1a", cursor.getString(4));
                map.put("foto2a", cursor.getString(5));
                map.put("foto3a", cursor.getString(6));
                map.put("foto1d", cursor.getString(7));
                map.put("foto2d", cursor.getString(8));
                map.put("foto3d", cursor.getString(9));
                map.put("inicio", cursor.getString(10));
                map.put("fim", cursor.getString(11));
                map.put("observacaoantes", cursor.getString(12));
                map.put("observacaodepois", cursor.getString(13));
                map.put("latitude", cursor.getString(14));
                map.put("longitude", cursor.getString(15));
                map.put("situacao", cursor.getString(16));
                map.put("dataencerramento", cursor.getString(23));
                map.put("assinaturaColaborador", cursor.getString(24));
                map.put("assinaturaCliente", cursor.getString(25));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(wordList);
    }


    public ArrayList<HashMap<String, String>> getMedicoes() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM  " + TABELA_MEDICAO;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ordemservico", cursor.getString(1));
                map.put("leitura_agua1", cursor.getString(2));
                map.put("leitura_agua2", cursor.getString(3));
                map.put("leitura_energia1", cursor.getString(4));
                map.put("leitura_energia2", cursor.getString(5));
                map.put("centrocusto_id", cursor.getString(6));
                map.put("data", cursor.getString(8));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }


    public String gravarMedicao(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABELA_MEDICAO +" where status = '"+"A"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ordemservico", cursor.getString(1));
                map.put("leitura_agua1", cursor.getString(2));
                map.put("leitura_agua2", cursor.getString(3));
                map.put("leitura_energia1", cursor.getString(4));
                map.put("leitura_energia2", cursor.getString(5));
                map.put("centrocusto_id", cursor.getString(6));
                map.put("data", cursor.getString(8));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(wordList);
    }




    /**
     * Update Sync status against each User ID
     * @param observacaodepois;
     * @param horariodepois;
     * @param assinaturaColaborador;
     * @param update_Status;
     */
    public void updateOS(String os, String observacaodepois, String horariodepois, String dataencerramento, String assinaturaColaborador, String update_Status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set observacaodepois = '" + observacaodepois + "', fim = '" + horariodepois + "', dataencerramento = '" + dataencerramento + "', assinaturaColaborador = '" + assinaturaColaborador  + "',  update_Status = '" + update_Status +"' where ordemservico_id ="+"'"+ os +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    /**
     * Update Sync status against each User ID
     * @param ordemservico;
     * @param assinaturaCliente;
     */
    public void updateAssinaturaCliente(String ordemservico, String assinaturaCliente){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set assinaturaCliente = '" + assinaturaCliente +"' where ordemservico_id ="+"'"+ ordemservico +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    /**
     * Update Sync status against each User ID
     * @param ordemservico;
     * @param assinaturaColaborador;
     */
    public void updateAssinaturaColaborador(String ordemservico, String assinaturaColaborador){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set assinaturaColaborador = '" + assinaturaColaborador +"' where ordemservico_id ="+"'"+ ordemservico +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    /**
     * Update Sync status against each User ID
     * @param ordemservico;
     * @param checklist_id;
     * @param checklistitens_id;
     * @param medicao1;
     */
    public void updateMedicao1(String ordemservico, String checklist_id, String checklistitens_id, String medicao1){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set medida1 = '" + medicao1 +"' where " + COL_1 + " = "+"'"+ ordemservico + "'" + " AND "+ COL_2 +"='"+ checklist_id + "'" + " AND "+ COL_3 +"='"+ checklistitens_id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    /**
     * Update Sync status against each User ID
     * @param ordemservico;
     * @param checklist_id;
     * @param checklistitens_id;
     * @param medicao2;
     */
    public void updateMedicao2(String ordemservico, String checklist_id, String checklistitens_id, String medicao2){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set medida2 = '" + medicao2 +"' where " + COL_1 + " = "+"'"+ ordemservico + "'" + " AND "+ COL_2 +"='"+ checklist_id + "'" + " AND "+ COL_3 +"='"+ checklistitens_id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }



    // Gravar Foto

    /**
     * @param foto;
     */
    public void updateFotoCaminho1a(String os, String foto){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set foto1a = '" + foto  +"' where ordemservico_id ="+"'"+ os +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    /**
     * @param foto;
     */
    public void updateFotoCaminho2a(String os, String checklist_id, String checklistitens_id, String foto){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set foto2a = '" + foto  +"' where " + COL_1 + " = "+"'"+ os + "'" + " AND "+ COL_2 +"='"+ checklist_id + "'" + " AND "+ COL_3 +"='"+ checklistitens_id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    /**
     * @param foto;
     */
    public void updateFotoCaminho3a(String os, String checklist_id, String checklistitens_id, String foto){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set foto3a = '" + foto  +"' where  ="+"'"+ os +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    /**
     * @param foto;
     */
    public void updateFotoCaminho1d(String os, String checklist_id, String checklistitens_id, String foto){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set foto1d = '" + foto  +"' where ordemservico_id ="+"'"+ os +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    /**
     * @param foto;
     */
    public void updateFotoCaminho2d(String os, String checklist_id, String checklistitens_id, String foto){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set foto2d = '" + foto  +"' where ordemservico_id ="+"'"+ os +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    /**
     * @param foto;
     */
    public void updateFotoCaminho3d(String os, String checklist_id, String checklistitens_id, String foto){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set foto3d = '" + foto  +"' where ordemservico_id ="+"'"+ os +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    public boolean insertNA(String ordemservico_id, String checklist_id, String checklistitens_id, String foto1a, String foto2a, String foto3a, String foto1d, String foto2d, String foto3d, String inicio, String fim, String observacaoantes, String observacaodepois, String latitude, String longitude, String situacao, String dataencerramento, String update_Status ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ordemservico_id);
        contentValues.put(COL_2,checklist_id);
        contentValues.put(COL_3,checklistitens_id);
        contentValues.put(COL_4, foto1a);
        contentValues.put(COL_5, foto2a);
        contentValues.put(COL_6, foto3a);
        contentValues.put(COL_7, foto1d);
        contentValues.put(COL_8, foto2d);
        contentValues.put(COL_9, foto3d);
        contentValues.put(COL_10, inicio);
        contentValues.put(COL_11, fim);
        contentValues.put(COL_12, observacaoantes);
        contentValues.put(COL_13, observacaodepois);
        contentValues.put(COL_14, latitude);
        contentValues.put(COL_15, longitude);
        contentValues.put(COL_16, situacao);
        contentValues.put(COL_23, dataencerramento);
        contentValues.put(COL_26, update_Status);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public int dbSyncYes(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where update_Status = '"+"yes"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    public int dbSyncNO(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where update_Status = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    public int dbSyncMedicao(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABELA_MEDICAO + " where status = '"+"A"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     * @param ordemservico
     * @param status
     */
    public void updateSyncStatus(String ordemservico, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_NAME + " set update_Status = '"+ status +"' where ordemservico_id="+"'"+ ordemservico +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void updateSyncStatusMedicao(String ordemservico, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABELA_MEDICAO + " set status = '"+ status +"' where ordemservico ="+"'"+ ordemservico +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public Cursor getOS(String id, String itemAtual){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_1 + " = '" + id + "'" + " AND "+ COL_3 +"='"+ itemAtual + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void deleteAtividade(String value, String ordemservico)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+ COL_3 +"='"+ value+ "'" + " AND "+ COL_1 +"='"+ ordemservico + "'");
        db.close();
    }

    public void deleteOS(String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+ COL_1 +"='"+ value+ "'");
        db.close();
    }

    public void deleteDados()
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME;
        String sqlMedicao = "DELETE FROM " + TABELA_MEDICAO;
        db.execSQL(sql);
        db.execSQL(sqlMedicao);
    }


    public Integer deleteData (String ordemservico_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ordemservico_id = ?",new String[] {ordemservico_id});
    }
}