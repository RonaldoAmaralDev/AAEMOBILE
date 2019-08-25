package com.delaroystudios.uploadmedia.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.operacao.Atividade_Antes;
import com.delaroystudios.uploadmedia.visitas.MainActivityAtividades;

import static android.content.Context.MODE_PRIVATE;


public class OsAdapter extends RecyclerView.Adapter<OsAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private String tiposervico, id_Atividade, tiposervico_id, tiposolicitacaoDescricao, tiposervicoDescricao;

    BancoGeral myBDGeral;


    public OsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

        myBDGeral = new BancoGeral(mContext);
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView os;
        public TextView dataplanejamento;
        public TextView equipamentoos;
        public TextView localos;
        public TextView tiposervico;
        public TextView tiposolicitacao;
        public TextView descricao;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            os = itemView.findViewById(R.id.os);
            dataplanejamento = itemView.findViewById(R.id.dataplanejamento);
            equipamentoos = itemView.findViewById(R.id.equipamentoos);
            localos = itemView.findViewById(R.id.localos);
            tiposolicitacao = itemView.findViewById(R.id.tiposolicitacao);
            tiposervico = itemView.findViewById(R.id.tiposervicoos);
            descricao = itemView.findViewById(R.id.descricaopadraoos);
        }
    }

    @Override
    public OsAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.os_row_line , parent, false);
        return new OsAdapter.GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OsAdapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_OS));
        String checklist_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CHECKLIST_OS));
        String dataplanejamento = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DATAPLANEJAMENTO_OS));
        String equipamento_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_EQUIPAMENTO_OS));
        String local_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LOCAL_OS));
        String id_centrolucro = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CENTROCUSTO_OS));
        String descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAOPADRAO_OS));
        String quantItens= String.valueOf(myBDGeral.dbCountItem(checklist_id));


        holder.os.setText("OS: " + id);
        holder.dataplanejamento.setText("Data Programação: " + dataplanejamento);
        holder.descricao.setText("Descrição: " + descricao);


        Cursor dataLocal = myBDGeral.qrCode(local_id);
        while (dataLocal.moveToNext()) {

            String localdescricao = dataLocal.getString(3);
            holder.localos.setText("Local: " + localdescricao);

        }

        Cursor dataColaborador = myBDGeral.verificaEquipamento(equipamento_id);
        while (dataColaborador.moveToNext()) {

            String codigoEquipamento = dataColaborador.getString(1);
            String equipamento = dataColaborador.getString(2);
            holder.equipamentoos.setText("Equipamento: " + codigoEquipamento + " - " + equipamento);
        }
            String tiposervico_os = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_TIPOSERVICOID_OS));
            String tiposolicitacao_os = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_TIPOSOLICITACAOID_OS));

            Cursor dataTipoSolicitacao = myBDGeral.buscaTipoSolicitacao(tiposolicitacao_os);
            while (dataTipoSolicitacao.moveToNext()) {
                 tiposolicitacaoDescricao = dataTipoSolicitacao.getString(1);
                holder.tiposolicitacao.setText("Tipo Solicitação: " + tiposolicitacaoDescricao);
            }

            Cursor dataTipoServico = myBDGeral.buscaTipoServico(tiposervico_os);
            while (dataTipoServico.moveToNext()) {
                tiposervicoDescricao = dataTipoServico.getString(1);
                holder.tiposervico.setText("Tipo Serviço: " + tiposervicoDescricao);
            }



        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat sdfData =
                new java.text.SimpleDateFormat("dd/MM/yyyy");
        String currentTime = sdf.format(dt);
        String data = sdfData.format(dt);

            holder.itemView.setOnClickListener(v -> { // Linguagem Java 8

                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.logo)
                            .setTitle(R.string.app_name)
                            .setMessage("Deseja iniciar a OS: " + id +  "\nQuantidade Atividades: " + quantItens + "\nData: " + data + "\nHorario: " + currentTime)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences pref2 = mContext.getSharedPreferences("info", MODE_PRIVATE);
                                        String nameColaborador = pref2.getString("name", "");
                                        String emailColaborador= pref2.getString("email", "");
                                        String id_colaborador = pref2.getString("id", "" );
                                        String tipoColaborador = pref2.getString("tipo", "");

                                        //Começar a contar o HH
                                        myBDGeral.gravarHHInicio(
                                                id,
                                                id_centrolucro,
                                                currentTime,
                                                id_colaborador);

                                    SharedPreferences.Editor dados = mContext.getSharedPreferences("visita", MODE_PRIVATE).edit();
                                    dados.putString("os_id", id);
                                    dados.putString("checklist_id", checklist_id);
                                    dados.putString("equipamento_id", equipamento_id);
                                    dados.putString("local_id", local_id);
                                    dados.putString("dataplanejamento", dataplanejamento);
                                    dados.putString("tiposervico", tiposolicitacao_os);
                                    dados.putString("id_centrolucro", id_centrolucro);
                                    // Armazena as Preferencias
                                    dados.commit();

                                    Intent intent = new Intent(mContext, MainActivityAtividades.class);
                                    Bundle dados2 = new Bundle();
                                    dados2.putString("os_id", id);
                                    dados2.putString("name", nameColaborador);
                                    dados2.putString("email", emailColaborador);
                                    dados2.putString("id_colaborador", id_colaborador);
                                    dados2.putString("tipo", tipoColaborador);
                                    dados2.putString("checklist_id", checklist_id);
                                    dados2.putString("equipamento_id", equipamento_id);
                                    dados2.putString("local_id", local_id);
                                    intent.putExtras(dados2);
                                    mContext.startActivity(intent);
                                    }
                            })
                            .setNegativeButton("Não", null)
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