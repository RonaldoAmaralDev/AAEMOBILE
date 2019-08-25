package com.delaroystudios.uploadmedia.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.banco.DatabaseHelper;
import com.delaroystudios.uploadmedia.operacao.Atividade_Antes;
import com.delaroystudios.uploadmedia.operacao.AbrirCorretiva;
import com.delaroystudios.uploadmedia.operacao.os.MainActivityOS;
import com.delaroystudios.uploadmedia.visitas.MainActivityAtividades;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;

import static android.content.Context.MODE_PRIVATE;


public class EquipamentoAdapter extends RecyclerView.Adapter<EquipamentoAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    BancoGeral myBDGeral;
    DatabaseHelper myBDOperacao;
    String ordemservico, id, centrolucro_id, local_id, id_Atividade, fabricante, leitura_agua1, leitura_agua2, leitura_energia1, leitura_energia2;


    public EquipamentoAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        myBDGeral = new BancoGeral(mContext);
        myBDOperacao = new DatabaseHelper(mContext);

    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView codigoequipamento;
        public TextView descricao;
        public TextView modeloequipamento;
        public TextView numeroserie;
        public TextView btuequipamento;
        public TextView fabricante;
        public ImageView logo_equipamento;

        public GroceryViewHolder(View itemView) {
            super(itemView);


            logo_equipamento = itemView.findViewById(R.id.logo_equipamento);
            codigoequipamento = itemView.findViewById(R.id.placaveiculo);
            descricao = itemView.findViewById(R.id.descricaoveiculo);
            modeloequipamento = itemView.findViewById(R.id.modeloveiculo);
            numeroserie = itemView.findViewById(R.id.contratoveiculo);
            btuequipamento = itemView.findViewById(R.id.cidadeveiculo);
            fabricante = itemView.findViewById(R.id.fabricanteequipamento);
        }
    }

    @Override
    public EquipamentoAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.equipamento_row_line, parent, false);
        return new EquipamentoAdapter.GroceryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(EquipamentoAdapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_EQUIPAMENTO));
        local_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LOCAL_EQUIPAMENTO));
        String name = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CODIGOEQUIPAMENTO_EQUIPAMENTO));
        centrolucro_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CENTROCUSTO_EQUIPAMENTO));
        String descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_EQUIPAMENTO));
        String modelo = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_MODELO_EQUIPAMENTO));
        String numeroserie = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_NUMEROSERIE_EQUIPAMENTO));
        String btu = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_BTU_EQUIPAMENTO));
        fabricante = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_FABRICANTE_EQUIPAMENTO));

        holder.codigoequipamento.setText("Codigo: " + name);
        holder.descricao.setText("Descrição: " + descricao);
        holder.modeloequipamento.setText("Modelo: " + modelo);
        holder.numeroserie.setText("Nª Serie: " + numeroserie);
        holder.fabricante.setText("Fabricante: " + fabricante);
        holder.btuequipamento.setText("BTU: " + btu);
        holder.logo_equipamento.setImageResource(R.drawable.ic_equipamento);

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8


            new AlertDialog.Builder(mContext)
                    .setIcon(R.drawable.logo)
                    .setTitle(R.string.app_name)
                    .setMessage("Deseja: ")
                    .setPositiveButton("Visitas Abertas", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(mContext, MainActivityOS.class);
                            Bundle dados = new Bundle();
                            dados.putString("equipamento_id", id);
                            dados.putString("local_id", local_id);
                            dados.putString("centrolucro_id", centrolucro_id);
                            intent.putExtras(dados);
                            mContext.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Abrir Visita", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Escolha Tipo Solicitação: ");
                            String[] tiposolicitacaos = {"CORRETIVA", "ORÇAMENTO", "PREDITIVA", "LIMPEZA", "INSPEÇÃO"};
                            builder.setItems(tiposolicitacaos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        // Se marcar Corretiva irá para Classe para abrir Visita
                                        case 0:
                                            Intent intent = new Intent(mContext, AbrirCorretiva.class);
                                            Bundle dados = new Bundle();
                                            dados.putString("equipamento_id", id);
                                            dados.putString("local_id", local_id);
                                            dados.putString("centrolucro_id", centrolucro_id);
                                            intent.putExtras(dados);
                                            mContext.startActivity(intent);
                                        break;
                                        //Fim Marcar Corretiva

                                        // Se marcar Orçamento
                                        case 1:
                                            Toast.makeText(mContext, "Em Desenvolvimento...", Toast.LENGTH_LONG).show();

                                            break;
                                        // Fim Marcar Orçamento

                                        // Se marcar Preditiva
                                        case 2:

                                            Toast.makeText(mContext, "Em Desenvolvimento...", Toast.LENGTH_LONG).show();

                                            break;


                                        // Fim Marcar Preditiva

                                        // Se marcar Limpeza
                                        case 3:
                                            String data = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                                            SharedPreferences pref = mContext.getSharedPreferences("info", MODE_PRIVATE);
                                            String name = pref.getString("name", "");
                                            String email= pref.getString("email", "");
                                            String colaborador_id = pref.getString("id", "" );
                                            String tipo = pref.getString("tipo", "");

                                            String URL = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";
                                            Ion.with(mContext)
                                                    .load(URL)
                                                    .setBodyParameter("colaborador_id", colaborador_id)
                                                    .setBodyParameter("local_id", local_id)
                                                    .setBodyParameter("centrocusto_id", centrolucro_id)
                                                    .setBodyParameter("tiposolicitacao_id", "5")
                                                    .setBodyParameter("tiposervico_id", "9")
                                                    .setBodyParameter("prioridade_id", "1")
                                                    .setBodyParameter("equipamento_id", id)
                                                    .setBodyParameter("frequencia_id", "1")
                                                    .setBodyParameter("impedimento_id", "1")
                                                    .setBodyParameter("checklist_id", "8")
                                                    .setBodyParameter("equipe1", colaborador_id)
                                                    .setBodyParameter("equipe2", colaborador_id)
                                                    .setBodyParameter("dataplanejamento", data)
                                                    .setBodyParameter("periodicidade", "1")
                                                    .setBodyParameter("descricaopadrao", "SERVIÇO LIMPEZA")
                                                    .setBodyParameter("descricaoservico", "SERVIÇO LIMPEZA")
                                                    .setBodyParameter("flag", "A")
                                                    .setBodyParameter("ativo", "A")
                                                    .asJsonObject()
                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                        @Override
                                                        public void onCompleted(Exception e, JsonObject result) {
                                                            try {
                                                                String RETORNO = result.get("LOGIN").getAsString();

                                                                if (RETORNO.equals("ERRO")) {
                                                                    Toast.makeText(mContext, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                                                } else if (RETORNO.equals("SUCESSO")) {

                                                                    // Armazenar dados no APP
                                                                    String ordemservico = result.get("ID").getAsString();
                                                                    String checklist_id = "8";

                                                                    myBDGeral.insertOS(
                                                                            ordemservico,
                                                                            local_id,
                                                                            centrolucro_id,
                                                                            "9",
                                                                            "LIMPEZA",
                                                                            id,
                                                                            "8",
                                                                            colaborador_id,
                                                                            data,
                                                                            "LIMPEZA",
                                                                            "aberta",
                                                                            "",
                                                                            "A");


                                                                    Toast.makeText(mContext, "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(mContext, MainActivityAtividades.class);
                                                                    Bundle dados = new Bundle();
                                                                    dados.putString("os_id", ordemservico);
                                                                    dados.putString("checklist_id", checklist_id);
                                                                    dados.putString("equipamento_id", id);
                                                                    dados.putString("local_id", local_id);
                                                                    dados.putString("dataplanejamento", data);
                                                                    dados.putString("tiposervico", "2");
                                                                    dados.putString("centrocusto_id", centrolucro_id);
                                                                    dados.putString("atividade", "Realizar Limpeza");
                                                                    dados.putString("name", name);
                                                                    dados.putString("email", email);
                                                                    dados.putString("colaborador_id", colaborador_id);
                                                                    dados.putString("tipo", tipo);
                                                                    intent.putExtras(dados);
                                                                    mContext.startActivity(intent);
                                                                } else {
                                                                    Toast.makeText(mContext, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                                                }
                                                            } catch (Exception erro) {
                                                                Toast.makeText(mContext, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                            break;
                                        // Fim se marcar Limpezar

                                        // Se marcar Inspeção
                                        case 4:
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                            builder.setTitle("Escolha Tipo Inspeção: ");
                                            String[] tiposervicos = {"INSPEÇÃO BOMBEIRO", "INSPEÇÃO ELETRICISTA"};
                                            builder.setItems(tiposervicos, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                        // Inicio Inspeção Bombeiro
                                                        case 0: //
                                                            String data = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                                                            SharedPreferences pref = mContext.getSharedPreferences("info", MODE_PRIVATE);
                                                            String name = pref.getString("name", "");
                                                            String email= pref.getString("email", "");
                                                            String colaborador_id = pref.getString("id", "" );
                                                            String tipo = pref.getString("tipo", "");

                                                            String URL = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";
                                                            Ion.with(mContext)
                                                                    .load(URL)
                                                                    .setBodyParameter("colaborador_id", colaborador_id)
                                                                    .setBodyParameter("local_id", local_id)
                                                                    .setBodyParameter("centrocusto_id", centrolucro_id)
                                                                    .setBodyParameter("tiposolicitacao_id", "6")
                                                                    .setBodyParameter("tiposervico_id", "11")
                                                                    .setBodyParameter("prioridade_id", "1")
                                                                    .setBodyParameter("equipamento_id", id)
                                                                    .setBodyParameter("frequencia_id", "1")
                                                                    .setBodyParameter("impedimento_id", "1")
                                                                    .setBodyParameter("checklist_id", "19")
                                                                    .setBodyParameter("equipe1", colaborador_id)
                                                                    .setBodyParameter("equipe2", colaborador_id)
                                                                    .setBodyParameter("dataplanejamento", data)
                                                                    .setBodyParameter("periodicidade", "1")
                                                                    .setBodyParameter("descricaopadrao", "INSPEÇÃO BOMBEIRO CIVIL")
                                                                    .setBodyParameter("descricaoservico", "INSPEÇÃO BOMBEIRO CIVIL")
                                                                    .setBodyParameter("flag", "A")
                                                                    .setBodyParameter("ativo", "A")
                                                                    .asJsonObject()
                                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                                        @Override
                                                                        public void onCompleted(Exception e, JsonObject result) {
                                                                            try {
                                                                                String RETORNO = result.get("LOGIN").getAsString();

                                                                                if (RETORNO.equals("ERRO")) {
                                                                                    Toast.makeText(mContext, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                                                                } else if (RETORNO.equals("SUCESSO")) {

                                                                                    // Armazenar dados no APP
                                                                                    String ordemservico = result.get("ID").getAsString();
                                                                                    String checklist_id = "19";

                                                                                    myBDGeral.insertOS(ordemservico,
                                                                                            local_id,
                                                                                            centrolucro_id,
                                                                                            "11",
                                                                                            "INSPEÇÃO",
                                                                                            id,
                                                                                            checklist_id,
                                                                                            colaborador_id,
                                                                                            data,
                                                                                            "INSPEÇÃO BOMBEIRO CIVIL",
                                                                                            "aberta",
                                                                                            "",
                                                                                            "A"
                                                                                    );

                                                                                    Toast.makeText(mContext, "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                                                    Intent intent = new Intent(mContext, MainActivityAtividades.class);
                                                                                    Bundle dados = new Bundle();
                                                                                    dados.putString("os_id", ordemservico);
                                                                                    dados.putString("checklist_id", checklist_id);
                                                                                    dados.putString("equipamento_id", id);
                                                                                    dados.putString("local_id", local_id);
                                                                                    dados.putString("dataplanejamento", data);
                                                                                    dados.putString("tiposervico", "11");
                                                                                    dados.putString("centrocusto_id", centrolucro_id);
                                                                                    dados.putString("atividade", "Realizar Inspeção Bombeiro");
                                                                                    dados.putString("name", name);
                                                                                    dados.putString("email", email);
                                                                                    dados.putString("colaborador_id", colaborador_id);
                                                                                    dados.putString("tipo", tipo);
                                                                                    intent.putExtras(dados);
                                                                                    mContext.startActivity(intent);
                                                                                } else {
                                                                                    Toast.makeText(mContext, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            } catch (Exception erro) {
                                                                                Toast.makeText(mContext, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                            break;
                                                            // Fim Inspeção Bombeiro

                                                        // Inicio Inspeção Eletricista
                                                        case 1:
                                                            SharedPreferences pref2 = mContext.getSharedPreferences("info", MODE_PRIVATE);
                                                            String nameColaborador = pref2.getString("name", "");
                                                            String emailColaborador= pref2.getString("email", "");
                                                            String id_colaborador = pref2.getString("id", "" );
                                                            String tipoColaborador = pref2.getString("tipo", "");
                                                            String dataVisita = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());


                                                            String URL_ABRIRVISITA = "http://helper.aplusweb.com.br/aplicativo/abrirCorretiva.php";
                                                            Ion.with(mContext)
                                                                    .load(URL_ABRIRVISITA)
                                                                    .setBodyParameter("colaborador_id", id_colaborador)
                                                                    .setBodyParameter("local_id", local_id)
                                                                    .setBodyParameter("centrocusto_id", centrolucro_id)
                                                                    .setBodyParameter("tiposolicitacao_id", "6")
                                                                    .setBodyParameter("tiposervico_id", "11")
                                                                    .setBodyParameter("prioridade_id", "1")
                                                                    .setBodyParameter("equipamento_id", id)
                                                                    .setBodyParameter("frequencia_id", "1")
                                                                    .setBodyParameter("impedimento_id", "1")
                                                                    .setBodyParameter("checklist_id", "20")
                                                                    .setBodyParameter("equipe1", id_colaborador)
                                                                    .setBodyParameter("equipe2", id_colaborador)
                                                                    .setBodyParameter("dataplanejamento", dataVisita)
                                                                    .setBodyParameter("periodicidade", "1")
                                                                    .setBodyParameter("descricaopadrao", "INSPEÇÃO DIARIA ELETRICISTA")
                                                                    .setBodyParameter("descricaoservico", "INSPEÇÃO DIARIA ELETRICISTA")
                                                                    .setBodyParameter("flag", "A")
                                                                    .setBodyParameter("ativo", "A")
                                                                    .asJsonObject()
                                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                                        @Override
                                                                        public void onCompleted(Exception e, JsonObject result) {
                                                                            try {
                                                                                String RETORNO = result.get("LOGIN").getAsString();

                                                                                if (RETORNO.equals("ERRO")) {
                                                                                    Toast.makeText(mContext, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                                                                } else if (RETORNO.equals("SUCESSO")) {

                                                                                    // Armazenar dados no APP
                                                                                    String ordemservico = result.get("ID").getAsString();
                                                                                    String checklist_id = "20";

                                                                                    myBDGeral.insertOS(
                                                                                            ordemservico,
                                                                                            local_id,
                                                                                            centrolucro_id,
                                                                                            "11",
                                                                                            "INSPEÇÃO DIARIA ELETRICISTA",
                                                                                            id,
                                                                                            checklist_id,
                                                                                            id_colaborador,
                                                                                            dataVisita,
                                                                                            "INSPEÇÃO DIARIA ELETRICISTA",
                                                                                            "aberta",
                                                                                            "",
                                                                                            "A"
                                                                                    );


                                                                                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                                                                    alert.setTitle(R.string.app_name);
                                                                                    alert.setIcon(R.drawable.logo);
                                                                                    alert.setCancelable(false);
                                                                                    alert.setMessage("Leitura COPASA: ");
                                                                                    final EditText input = new EditText(mContext);
                                                                                    input.setRawInputType(Configuration.KEYBOARD_12KEY);
                                                                                    alert.setView(input);
                                                                                    alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {

                                                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                                                            leitura_agua1 = input.getText().toString();

                                                                                            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                                                                            alert.setTitle(R.string.app_name);
                                                                                            alert.setIcon(R.drawable.logo);
                                                                                            alert.setCancelable(false);
                                                                                            alert.setMessage("Leitura IRRIGAÇÃO: ");
                                                                                            final EditText input = new EditText(mContext);
                                                                                            input.setRawInputType(Configuration.KEYBOARD_12KEY);
                                                                                            alert.setView(input);
                                                                                            alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                                                                    leitura_agua2 = input.getText().toString();

                                                                                                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                                                                                    alert.setTitle(R.string.app_name);
                                                                                                    alert.setIcon(R.drawable.logo);
                                                                                                    alert.setCancelable(false);
                                                                                                    alert.setMessage("Leitura CEMIG: ");
                                                                                                    final EditText input = new EditText(mContext);
                                                                                                    input.setRawInputType(Configuration.KEYBOARD_12KEY);
                                                                                                    alert.setView(input);
                                                                                                    alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
                                                                                                        public void onClick(DialogInterface dialog, int whichButton) {

                                                                                                            leitura_energia1 = input.getText().toString();

                                                                                                            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                                                                                            alert.setTitle(R.string.app_name);
                                                                                                            alert.setIcon(R.drawable.logo);
                                                                                                            alert.setCancelable(false);
                                                                                                            alert.setMessage("Leitura CEMIG PILOTIS: ");
                                                                                                            final EditText input = new EditText(mContext);
                                                                                                            input.setRawInputType(Configuration.KEYBOARD_12KEY);
                                                                                                            alert.setView(input);
                                                                                                            alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
                                                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                                                    leitura_energia2 = input.getText().toString();

                                                                                                                    java.util.Date dt = new java.util.Date();
                                                                                                                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                                                                    String currentTime = sdf.format(dt);

                                                                                                                    myBDOperacao.insertMedicao(
                                                                                                                            ordemservico,
                                                                                                                            leitura_agua1,
                                                                                                                            leitura_agua2,
                                                                                                                            leitura_energia1,
                                                                                                                            leitura_energia2,
                                                                                                                            centrolucro_id,
                                                                                                                            "A",
                                                                                                                            currentTime);

                                                                                                                    String data = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                                                                                                                    SharedPreferences pref = mContext.getSharedPreferences("info", MODE_PRIVATE);
                                                                                                                    String name = pref.getString("name", "");
                                                                                                                    String email= pref.getString("email", "");
                                                                                                                    String colaborador_id = pref.getString("id", "" );
                                                                                                                    String tipo = pref.getString("tipo", "");

                                                                                                                    Toast.makeText(mContext, "Chamado corretivo Nª " + ordemservico + ", aberto com sucesso. ", Toast.LENGTH_LONG).show();
                                                                                                                    Intent intent = new Intent(mContext, MainActivityAtividades.class);
                                                                                                                    Bundle dados = new Bundle();
                                                                                                                    dados.putString("os_id", ordemservico);
                                                                                                                    dados.putString("checklist_id", checklist_id);
                                                                                                                    dados.putString("equipamento_id", id);
                                                                                                                    dados.putString("local_id", local_id);
                                                                                                                    dados.putString("dataplanejamento", dataVisita);
                                                                                                                    dados.putString("tiposervico", "11");
                                                                                                                    dados.putString("centrocusto_id", centrolucro_id);
                                                                                                                    dados.putString("atividade", "Realizar Inspeção");
                                                                                                                    dados.putString("name", name);
                                                                                                                    dados.putString("email", email);
                                                                                                                    dados.putString("colaborador_id", colaborador_id);
                                                                                                                    dados.putString("tipo", tipo);
                                                                                                                    intent.putExtras(dados);
                                                                                                                    mContext.startActivity(intent);
                                                                                                                }
                                                                                                            });

                                                                                                            alert.show();
                                                                                                        }
                                                                                                    });

                                                                                                    alert.show();

                                                                                                }
                                                                                            });

                                                                                            alert.show();
                                                                                        }

                                                                                    });

                                                                                    alert.show();

                                                                                } else {
                                                                                    Toast.makeText(mContext, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            } catch (Exception erro) {
                                                                                Toast.makeText(mContext, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                            break;


                                                    }
                                                }
                                            });
                                            AlertDialog dialogTipoSolicitacao = builder.create();
                                            dialogTipoSolicitacao.show();
                                            break;
                                    }
                                }
                            });
                            AlertDialog dialogTipoSolicitacao = builder.create();
                            dialogTipoSolicitacao.show();

                        }
                    })

                    .show();

        });
    }






    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        //try using this
        if(mCursor==null)
            return 0;
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
