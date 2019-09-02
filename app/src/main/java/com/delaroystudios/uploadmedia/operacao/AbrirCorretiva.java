package com.delaroystudios.uploadmedia.operacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;
import com.delaroystudios.uploadmedia.visitas.MainActivityAtividades;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;

public class AbrirCorretiva extends AppCompatActivity {

    BancoGeral myBDGeral;
    String tipo, id_Atividade, id_1Atividade, preventiva, corretiva, btu, codigo, equipe, email, name, colaborador_id, prioridade, tiposervico, local_id, id_equipamento, equipamento, modelo, centrocusto_id, localdescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_corretiva);


        // Deixa Tela Cheia
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        myBDGeral = new BancoGeral(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        id_equipamento = dados.getString("equipamento_id");
        local_id = dados.getString("local_id");
        centrocusto_id = dados.getString("centrolucro_id");

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        colaborador_id = pref.getString("id", "");
        tipo = pref.getString("tipo", "");


        iniciarAberturaCorretiva();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setCancelable(true)
                .setMessage("Deseja voltar para tela inicial: ")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AbrirCorretiva.this, MainActivity_Principal.class);
                        Bundle dados = new Bundle();
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("id", colaborador_id);
                        dados.putString("tipo", tipo);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    public boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public void iniciarAberturaCorretiva() {

        if (verificaConexao() == false) {
            Toast.makeText(getApplicationContext(), "Você não está conectado na Internet.", Toast.LENGTH_LONG).show();
        } else {
            perguntarCodigoCliente();
        }

    }

    public void perguntarCodigoCliente() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);
        alert.setIcon(R.drawable.logo);
        alert.setCancelable(false);
        alert.setMessage("Número chamado sistema cliente: ");
        final EditText input = new EditText(this);
        input.setHintTextColor(getResources().getColor(R.color.white));
        alert.setView(input);
        alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (input.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Número Chamado não pode está vazio.", Toast.LENGTH_LONG).show();
                } else if (input.getText().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Número Chamado deve ter mais que 3 caracteres..", Toast.LENGTH_LONG).show();
                } else {
                    String codigocliente = input.getText().toString();
                    abrirCorretiva(codigocliente);
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(AbrirCorretiva.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", tipo);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });
        alert.setNeutralButton("Não se Aplica", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Continuar abertura corretiva mas sem codigo chamado
                abrirCorretiva("");
            }
        });
        alert.show();
    }

    public void abrirCorretiva(String codigocliente) {

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email = pref.getString("email", "");
        String colaborador_id = pref.getString("id", "");
        String tipo = pref.getString("tipo", "");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AbrirCorretiva.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha Tipo Serviço: ");
        String[] tiposervicos = {"AR CONDICIONADO", "REFRIGERAÇÃO", "ELETRICA", "HIDRAULICA", "CIVIL", "PREDIAL", "JARDINAGEM", "LIMPEZA", "INFORMATICA"};
        builder.setItems(tiposervicos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // Inicio AR CONDICIONADO
                    case 0: //

                        prioridade = "1";
                        tiposervico = "1";
                        String data = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A");

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        // Fim AR CONDICIONADO
                        break;
                    // INICIO REFRIGERAÇÃO
                    case 1:

                        prioridade = "1";
                        tiposervico = "2";
                        String data2 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR2 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL2 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL2)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data2)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data2);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data2,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        // FIM REFRIGERAÇÃO
                        break;
                    // INICIO ELETRICA
                    case 2:

                        prioridade = "1";
                        tiposervico = "3";
                        String data3 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                        String URL3 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL3)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data3)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data3);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data3,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        // FIM ELETRICA
                        break;
                    //INICIO HIDRAULICA
                    case 3:

                        prioridade = "1";
                        tiposervico = "4";
                        String data4 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR4 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL4 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL4)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data4)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data4);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                        //FIM HIDRAULICA
                        break;
                    // INICIO CIVIL
                    case 4:


                        prioridade = "1";
                        tiposervico = "5";
                        String data5 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR5 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL5 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL5)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data5)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data5);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data5,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                        // FIM CIVIL
                        break;
                    // INICIO PREDIAL
                    case 5:


                        prioridade = "1";
                        tiposervico = "6";
                        String data6 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR6 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL6 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL6)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data6)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data6);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data6,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                        // FIM PREDIAL
                        break;
                    // INICIO JARDINAGEM
                    case 6:


                        prioridade = "1";
                        tiposervico = "8";
                        String data7 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR7 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL7 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";


                        Ion.with(AbrirCorretiva.this)
                                .load(URL7)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data7)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data7);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data7,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                        // FIM JARDINAGEM
                        break;
                    // INICIO LIMPEZA
                    case 7:

                        prioridade = "1";
                        tiposervico = "9";
                        String data8 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR8 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL8 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL8)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data8)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data8);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data8,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);

                                                Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("os_id", ordemservico);
                                                dados.putString("checklist_id", checklist_id);
                                                dados.putString("equipamento_id", id_equipamento);
                                                dados.putString("local_id", local_id);
                                                dados.putString("dataplanejamento", data);
                                                dados.putString("tiposervico", "2");
                                                dados.putString("centrocusto_id", centrocusto_id);
                                                dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("idColaborador", colaborador_id);
                                                dados.putString("tipo", tipo);
                                                intent.putExtras(dados);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        // FIM LIMPEZA
                        break;
                    // INICIO INFORMATICA
                    case 8:
                        prioridade = "1";
                        tiposervico = "10";
                        String data9 = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                        String dataPadraoBR9 = new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());

                        String URL9 = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";

                        Ion.with(AbrirCorretiva.this)
                                .load(URL9)
                                .setBodyParameter("colaborador_id", colaborador_id)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("centrocusto_id", centrocusto_id)
                                .setBodyParameter("tiposolicitacao_id", "2")
                                .setBodyParameter("tiposervico_id", tiposervico)
                                .setBodyParameter("prioridade_id", prioridade)
                                .setBodyParameter("equipamento_id", id_equipamento)
                                .setBodyParameter("frequencia_id", "7")
                                .setBodyParameter("impedimento_id", "1")
                                .setBodyParameter("checklist_id", "8")
                                .setBodyParameter("equipe1", colaborador_id)
                                .setBodyParameter("equipe2", colaborador_id)
                                .setBodyParameter("dataplanejamento", data9)
                                .setBodyParameter("periodicidade", "1")
                                .setBodyParameter("descricaopadrao", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("descricaoservico", "CORRETIVA GERADA PELO CELULAR")
                                .setBodyParameter("flag", "A")
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {

                                                // Armazenar dados no APP
                                                String ordemservico = result.get("ID").getAsString();
                                                String checklist_id = "8";

                                                SharedPreferences.Editor dados2 = getSharedPreferences("visita", MODE_PRIVATE).edit();
                                                dados2.putString("os_id", ordemservico);
                                                dados2.putString("checklist_id", checklist_id);
                                                dados2.putString("equipamento_id", id_equipamento);
                                                dados2.putString("local_id", local_id);
                                                dados2.putString("dataplanejamento", data9);
                                                dados2.putString("tiposervico", tiposervico);
                                                dados2.putString("id_centrolucro", centrocusto_id);
                                                // Armazena as Preferencias
                                                dados2.commit();

                                                myBDGeral.insertOS(
                                                        ordemservico,
                                                        local_id,
                                                        centrocusto_id,
                                                        "2",
                                                        "CORRETIVA",
                                                        id_equipamento,
                                                        checklist_id,
                                                        colaborador_id,
                                                        data9,
                                                        "CORRETIVA GERADA PELO CELULAR",
                                                        "aberta",
                                                        "",
                                                        "A"
                                                );

                                                SharedPreferences pref2 = getApplication().getSharedPreferences("info", MODE_PRIVATE);
                                                String nameColaborador = pref2.getString("name", "");
                                                String emailColaborador = pref2.getString("email", "");
                                                String id_colaborador = pref2.getString("id", "");
                                                String tipoColaborador = pref2.getString("tipo", "");

                                                java.util.Date dt = new java.util.Date();
                                                java.text.SimpleDateFormat sdf =
                                                        new java.text.SimpleDateFormat("HH:mm:ss");
                                                java.text.SimpleDateFormat sdfData =
                                                        new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                String currentTime = sdf.format(dt);
                                                String data = sdfData.format(dt);

                                                //Começar a contar o HH
                                                myBDGeral.gravarHHInicio(
                                                        ordemservico,
                                                        centrocusto_id,
                                                        currentTime,
                                                        id_colaborador);


                                                    Toast.makeText(getApplicationContext(), "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(AbrirCorretiva.this, MainActivityAtividades.class);
                                                    Bundle dados = new Bundle();
                                                    dados.putString("os_id", ordemservico);
                                                    dados.putString("checklist_id", checklist_id);
                                                    dados.putString("equipamento_id", id_equipamento);
                                                    dados.putString("local_id", local_id);
                                                    dados.putString("dataplanejamento", data);
                                                    dados.putString("tiposervico", "2");
                                                    dados.putString("centrocusto_id", centrocusto_id);
                                                    dados.putString("atividade", "Realizar Manutenção Corretiva");
                                                    dados.putString("name", name);
                                                    dados.putString("email", email);
                                                    dados.putString("idColaborador", colaborador_id);
                                                    dados.putString("tipo", tipo);
                                                    intent.putExtras(dados);
                                                    startActivity(intent);

                                            } else {
                                                Toast.makeText(AbrirCorretiva.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(AbrirCorretiva.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                        // FIM INFORMATICA
                        break;
                }
            }
        });
        builder.show();
    }
}








