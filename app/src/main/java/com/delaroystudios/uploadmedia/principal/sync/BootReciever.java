package com.delaroystudios.uploadmedia.principal.sync;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.model.CL;
import com.delaroystudios.uploadmedia.model.Equipamento;
import com.delaroystudios.uploadmedia.model.Contact;
import com.delaroystudios.uploadmedia.model.OS;
import com.delaroystudios.uploadmedia.model.TipoServico;
import com.delaroystudios.uploadmedia.model.TipoSolicitacao;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


import static com.android.volley.VolleyLog.TAG;
import static com.delaroystudios.uploadmedia.principal.MainActivity_Principal.PRIMARY_FOREGROUND_NOTIF_SERVICE_ID;


public class BootReciever extends Service  {


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

       executarSyncAutomatica();

        return START_STICKY;
    }

    public void executarSyncAutomatica(){
        Tiempo a = new Tiempo();
        a.execute();
    }
    public void hilo() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public class Tiempo extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            // i= Tempo
            // 900 = 15 minutos
            //7200 = 2 horas
            for(int i=0;i<7200;i++){
                hilo();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            //Ação a cada tempo determinado
            executarSyncAutomatica();
            verificaConexao();
        }
    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
            mandarLocalizacao();
            syncDados();
        } else {
            conectado = false;
        }
        return conectado;
    }

    public void emitirNotificacao() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "_channel_01";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, "notification", importance);
            mChannel.enableLights(true);
            BancoGeral myBDGeral;
            myBDGeral = new BancoGeral(this);
            SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
            String name = pref.getString("name", "");
            String email = pref.getString("email", "");
            String colaborador_id = pref.getString("id", "");
            String token = pref.getString("token", "");

            String quantidadeOS = String.valueOf(myBDGeral.dbCoutaberta(colaborador_id));
            int quantOS = Integer.parseInt(quantidadeOS);
            String dataSync = new SimpleDateFormat("HH:mm:ss -  dd/MM/yyyy ").format(System.currentTimeMillis());
            SharedPreferences.Editor prefSync = getSharedPreferences("data_sync", MODE_PRIVATE).edit();
            prefSync.putString("data_sync", dataSync);
            // Armazena as Preferencias
            prefSync.commit();

            Notification notification = new Notification.Builder(getApplicationContext(), id)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Você possui novas visitas para realizar.")
                    .setContentText("Visitas em aberto: " + quantOS)
                    .setSubText("Ultima sync: " + dataSync)
                    .build();

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification);
            }
        }
    }


    public void syncDados() {
        BancoGeral myBDGeral;
       JsonArrayRequest request ;
       RequestQueue requestQueue;
        TextView txtQuantImagens, txtHHTotal, txtHorarioInicial, txtVersionAtual, txtLocalizaçãoAtual, txtNameUsuario, txtQuantOSAberta, txtQuantOSSync, txtQuantLocais, txtQuantEquipamentos, txtQuantOSProgramadas, txtQuantOSEncerradas;
        String email, name, colaborador_id, token;
        String[] permissoes = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        myBDGeral = new BancoGeral(this);


        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        name = pref.getString("name", "");
        email= pref.getString("email", "");
        colaborador_id = pref.getString("id", "" );
        token = pref.getString("token", "");



        String URL = "http://helper.aplusweb.com.br/aplicativo/atualizarOperacao.php?colaborador_id=" + colaborador_id;
            request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    JSONObject jsonObject = null;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            CL cl = new CL();
                            Contact local = new Contact();
                            cl.setId(jsonObject.getString("idCL"));
                            cl.setCentrocusto(jsonObject.getString("centrocusto"));
                            cl.setDescricao(jsonObject.getString("descricao"));

                            Cursor data = myBDGeral.verificaCL(jsonObject.getString("idCL"));
                            if (data.moveToNext()) {
                                myBDGeral.updateCL(
                                        cl.getIdCL(),
                                        cl.getCentrocusto(),
                                        cl.getDescricao());
                            } else {
                                myBDGeral.insertCentroCusto(cl.getIdCL(), cl.getCentrocusto(), cl.getDescricao());
                            }
                            data.close();

                            local.setId(jsonObject.getString("idLocal"));
                            local.setCodigolocal(jsonObject.getString("codigolocal"));
                            local.setCentrocusto_id(jsonObject.getString("centrocusto_idLocal"));
                            local.setDescricaolocal(jsonObject.getString("descricaolocal"));
                            local.setBairro(jsonObject.getString("bairro"));
                            local.setCidade(jsonObject.getString("cidade"));
                            local.setSigla(jsonObject.getString("sigla"));
                            local.setEstado(jsonObject.getString("estado"));
                            //local.setLatitude(jsonObject.getString("latitude"));
                           // local.setLongitude(jsonObject.getString("longitude"));
                            local.setTempogasto(jsonObject.getString("tempogasto"));
                            local.setRegiaoID(jsonObject.getString("regiaoID"));
                            local.setRegiaoDescricao(jsonObject.getString("regiaoDescricao"));
                            local.setPoloatendimentosID(jsonObject.getString("poloatendimentosID"));
                            local.setPoloatenidmentosDescricao(jsonObject.getString("poloatendimentosDescricao"));
                            local.setContatoID(jsonObject.getString("contatoID"));
                            local.setContatoCLID(jsonObject.getString("contatoCLID"));
                            local.setContatoNome(jsonObject.getString("contatoNome"));
                            local.setContatoEndereco(jsonObject.getString("contatoEndereco"));
                            local.setContatoLatitude(jsonObject.getString("contatoLatitude"));
                            local.setContatoLongitude(jsonObject.getString("contatoLongitude"));
                            local.setAreaconstruida(jsonObject.getString("areaconstruida"));
                            local.setAreacapina(jsonObject.getString("areacapina"));
                            local.setEnderecolocal(jsonObject.getString("enderecolocal"));

                            Cursor dataLocal = myBDGeral.verificaLocal(jsonObject.getString("idLocal"));
                            if (dataLocal.moveToNext()) {
                            } else {
                                myBDGeral.insertLocal(
                                        local.getId(),
                                        local.getCodigolocal(),
                                        local.getCentrocusto_id(),
                                        local.getDescricaolocal(),
                                        local.getBairro(),
                                        local.getCidade(),
                                        "-19.860786",
                                        "-44.0052989",
                                        local.getSigla(),
                                        local.getEstado(),
                                        local.getTempogasto(),
                                        local.getRegiaoID(),
                                        local.getRegiaoDescricao(),
                                        local.getPoloatendimentosID(),
                                        local.getPoloatenidmentosDescricao(),
                                        local.getContatoID(),
                                        local.getContatoCLID(),
                                        local.getContatoNome(),
                                        local.getContatoEndereco(),
                                        local.getContatoLatitude(),
                                        local.getContatoLongitude(),
                                        local.getAreaconstruida(),
                                        local.getAreacapina(),
                                        local.getEnderecolocal(),
                                        local.getFrequencia(),
                                        local.getRaio(),
                                        local.getSituacao());
                            }
                            myBDGeral.updateLocal(
                                    local.getId(),
                                    local.getCodigolocal(),
                                    local.getCentrocusto_id(),
                                    local.getDescricaolocal(),
                                    local.getBairro(),
                                    local.getCidade(),
                                    "-19.860786",
                                    "-44.0052989",
                                    local.getSigla(),
                                    local.getEstado(),
                                    local.getTempogasto(),
                                    local.getRegiaoID(),
                                    local.getRegiaoDescricao(),
                                    local.getPoloatendimentosID(),
                                    local.getPoloatenidmentosDescricao(),
                                    local.getContatoID(),
                                    local.getContatoCLID(),
                                    local.getContatoNome(),
                                    local.getContatoEndereco(),
                                    local.getContatoLatitude(),
                                    local.getContatoLongitude(),
                                    local.getAreaconstruida(),
                                    local.getAreacapina(),
                                    local.getEnderecolocal());

                            dataLocal.close();

                            Equipamento equipamento = new Equipamento();
                            equipamento.setId(jsonObject.getString("idEquipamento"));
                            equipamento.setCodigoequipamento(jsonObject.getString("codigoequipamento"));
                            equipamento.setDescricaoequipamento(jsonObject.getString("descricaoequipamento"));
                            equipamento.setCentrocusto_id(jsonObject.getString("centrocusto_idEquipamento"));
                            equipamento.setLocal_id(jsonObject.getString("local_idEquipamento"));
                            equipamento.setModelo(jsonObject.getString("modelo"));
                            equipamento.setTag(jsonObject.getString("tag"));
                            equipamento.setNumeroserie(jsonObject.getString("numeroserie"));
                            equipamento.setBtu(jsonObject.getString("btu"));
                            equipamento.setFabricante(jsonObject.getString("fabricante"));
                            equipamento.setTipoequipamento(jsonObject.getString("tipoequipamento"));
                            equipamento.setFornecedor(jsonObject.getString("fornecedor"));

                            Cursor dataEquipamento = myBDGeral.verificaEquipamento(jsonObject.getString("idEquipamento"));
                            if (dataEquipamento.moveToNext()) {
                                myBDGeral.updateEquipamento(
                                        equipamento.getId(),
                                        equipamento.getCodigoequipamento(),
                                        equipamento.getDescricaoequipamento(),
                                        equipamento.getCentrocusto_id(),
                                        equipamento.getLocal_id(),
                                        equipamento.getModelo(),
                                        equipamento.getTag(),
                                        equipamento.getNumeroserie(),
                                        equipamento.getBtu(),
                                        equipamento.getFabricante(),
                                        equipamento.getTipoequipamento(),
                                        equipamento.getFornecedor());
                            } else {
                                myBDGeral.insertEquipamento(
                                        equipamento.getId(),
                                        equipamento.getCodigoequipamento(),
                                        equipamento.getDescricaoequipamento(),
                                        equipamento.getCentrocusto_id(),
                                        equipamento.getLocal_id(),
                                        equipamento.getFabricante(),
                                        equipamento.getTipoequipamento(),
                                        equipamento.getFornecedor());
                            }
                            myBDGeral.updateEquipamento(
                                    equipamento.getId(),
                                    equipamento.getCodigoequipamento(),
                                    equipamento.getDescricaoequipamento(),
                                    equipamento.getCentrocusto_id(),
                                    equipamento.getLocal_id(),
                                    equipamento.getModelo(),
                                    equipamento.getTag(),
                                    equipamento.getNumeroserie(),
                                    equipamento.getBtu(),
                                    equipamento.getFabricante(),
                                    equipamento.getTipoequipamento(),
                                    equipamento.getFornecedor());
                            dataEquipamento.close();

                            OS os = new OS();
                            os.setId(jsonObject.getString("idOS"));
                            os.setTiposolicitacao(jsonObject.getString("tiposolicitacao_os"));
                            os.setTiposervico(jsonObject.getString("tiposervico_os"));
                            os.setChecklist_id(jsonObject.getString("checklist_id"));
                            os.setCentrocusto_id(jsonObject.getString("centrocusto_id"));
                            os.setLocal_id(jsonObject.getString("local_id"));
                            os.setEquipamento_id(jsonObject.getString("equipamento_id"));
                            os.setEquipe1(jsonObject.getString("equipe1"));
                            os.setDataplanejamento(jsonObject.getString("dataplanejamento"));
                            os.setDescricaopadrao(jsonObject.getString("descricaopadrao"));
                            //   os.setCodigochamado(jsonObject.getString("codigochamado"));
                            os.setFlag_os(jsonObject.getString("flag_os"));

                            Cursor dataOS = myBDGeral.verificaOS(jsonObject.getString("idOS"));
                            if (dataOS.moveToNext()) {
                                myBDGeral.updateOS(
                                        os.getId(),
                                        os.getLocal_id(),
                                        os.getCentrocusto_id(),
                                        os.getTiposolicitacao(),
                                        os.getTiposervico(),
                                        os.getEquipamento_id(),
                                        os.getChecklist_id(),
                                        os.getEquipe1(),
                                        os.getDataplanejamento(),
                                        os.getDescricaopadrao(),
                                        os.getFlag_os());

                                myBDGeral.updateSituacaoOSSistematica(
                                        os.getId(),
                                        "A");

                            } else {
                                myBDGeral.insertOS(
                                        os.getId(),
                                        os.getLocal_id(),
                                        os.getCentrocusto_id(),
                                        os.getTiposolicitacao(),
                                        os.getTiposervico(),
                                        os.getEquipamento_id(),
                                        os.getChecklist_id(),
                                        os.getEquipe1(),
                                        os.getDataplanejamento(),
                                        os.getDescricaopadrao(),
                                        "aberta",
                                      //  os.getCodigochamado()
                                          "",
                                        os.getFlag_os());


                                // Ao inserir OS irá verificar se ela é preventiva para inserir situacao "A" assim depois criando nova
                                myBDGeral.updateSituacaoOSSistematica(
                                        os.getId(),
                                        "A");

                            }

                            TipoSolicitacao tipoSolicitacao = new TipoSolicitacao();
                            tipoSolicitacao.setId(jsonObject.getString("tiposolicitacao_id"));
                            tipoSolicitacao.setDescricao(jsonObject.getString("tiposolicitacao_descricao"));

                            Cursor dataTipoSolicitacao = myBDGeral.verificaTipoSolicitacao(jsonObject.getString("tiposolicitacao_id"));
                            if (dataTipoSolicitacao.moveToNext()) {
                                myBDGeral.updateTipoSolicitacao(
                                        tipoSolicitacao.getId(),
                                        tipoSolicitacao.getDescricao()
                                );
                            } else {
                                myBDGeral.insertTipoSolicitacao(
                                        tipoSolicitacao.getId(),
                                        tipoSolicitacao.getDescricao());

                            }

                            TipoServico tipoServico = new TipoServico();
                            tipoServico.setId(jsonObject.getString("tiposervico_id"));
                            tipoServico.setDescricao(jsonObject.getString("tiposervico_descricao"));

                            Cursor dataTipoServico = myBDGeral.verificaTipoServico(jsonObject.getString("tiposervico_id"));
                            if (dataTipoSolicitacao.moveToNext()) {
                                myBDGeral.updateTipoServico(
                                        tipoServico.getId(),
                                        tipoServico.getDescricao()
                                );
                            } else {
                                myBDGeral.insertTipoServico(
                                        tipoServico.getId(),
                                        tipoServico.getDescricao());
                            }


                           emitirNotificacao();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 20000;
            RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy2);
            requestQueue.add(request);

    }

    public void mandarLocalizacao() {

        String URL = "https://helper.aplusweb.com.br/aplicativo/enviarLocalizacao.php";

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String colaborador_id = pref.getString("id", "" );

        SharedPreferences salvarLocalizacao = getSharedPreferences("salvarLocalizacao", MODE_PRIVATE);
        String latitude = salvarLocalizacao.getString("latitude", "");
        String longitude= salvarLocalizacao.getString("longitude", "");

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat sdfData =
                new java.text.SimpleDateFormat("yyyy/MM/dd");
        String currentTime = sdf.format(dt);
        String data = sdfData.format(dt);
        String created_at = data + " " + currentTime;
        String updated_at = data + " " + currentTime;

        Ion.with(this)
                .load(URL)
                .setBodyParameter("colaborador_id", colaborador_id)
                .setBodyParameter("latitude", latitude)
                .setBodyParameter("longitude", longitude)
                .setBodyParameter("created_at", created_at)
                .setBodyParameter("updated_at", updated_at)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("LOCALIZACAO").getAsString();
                            if (RETORNO.equals("ERRO")) {
                            } else if (RETORNO.equals("SUCESSO")) {
                                // Sucesso ao enviar localizacao
                                //    String ordemservico = result.get("ID").getAsString();
                            } else {
                            }
                        } catch (Exception erro) {
                        }

                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}