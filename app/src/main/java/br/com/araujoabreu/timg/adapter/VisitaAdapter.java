package br.com.araujoabreu.timg.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.visitas.MainActivityAtividades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class VisitaAdapter extends RecyclerView.Adapter<VisitaAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private String tiposervico, id_Atividade, tiposervico_id, tiposolicitacaoDescricao, tiposervicoDescricao;

    BancoGeral myBDGeral;


    public VisitaAdapter(Context context, Cursor cursor) {
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
        public TextView frequencia;
        public TextView descricao;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            os = itemView.findViewById(R.id.os);
            dataplanejamento = itemView.findViewById(R.id.dataplanejamento);
            equipamentoos = itemView.findViewById(R.id.equipamentoos);
            localos = itemView.findViewById(R.id.localos);
            tiposolicitacao = itemView.findViewById(R.id.tiposolicitacao);
            tiposervico = itemView.findViewById(R.id.tiposervicoos);
            frequencia = itemView.findViewById(R.id.frequenciaos);
            descricao = itemView.findViewById(R.id.descricaopadraoos);
        }
    }

    @Override
    public VisitaAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.os_row_line , parent, false);
        return new VisitaAdapter.GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VisitaAdapter.GroceryViewHolder holder, int position) {
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
        String quantItens = String.valueOf(myBDGeral.dbCountItem(checklist_id));
        String frequencia = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_FREQUENCIA_DESCRICAO));

        holder.os.setText("OS: " + id);
        holder.dataplanejamento.setText("Data Programação: " + dataplanejamento);
        holder.descricao.setText("Descrição: " + descricao);
        holder.frequencia.setText("Frequencia: " + frequencia);


        Cursor dataLocal = myBDGeral.qrCode(local_id);
        while (dataLocal.moveToNext()) {

            String localdescricao = dataLocal.getString(3);
            holder.localos.setText("Local: " + localdescricao);

        Cursor dataColaborador = myBDGeral.verificaEquipamento(equipamento_id);
        while (dataColaborador.moveToNext()) {

            String codigoEquipamento = dataColaborador.getString(1);
            String equipamento = dataColaborador.getString(2);
            holder.equipamentoos.setText("Equipamento: " + codigoEquipamento + " - " + equipamento);


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
        String dataehora = currentTime + " - " + data;

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8
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


            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.dialog_iniciaros, null);
            view.animate();

            //Numero da Visita
            TextView txtNumeroVisita = view.findViewById(R.id.txtNumerodaVisita);
            txtNumeroVisita.setText("Visita: " + id);

            //SubLocal - Estação/Loja ou Equipamento
            TextView txtEquipamentoVisita = view.findViewById(R.id.txtEnderecoLocal);
            txtEquipamentoVisita.setText("Equipamento: " + codigoEquipamento);

            //Local da Visita
            TextView txtLocalVisita = view.findViewById(R.id.txtCidadeeEstado);
            txtLocalVisita.setText("Local: " + localdescricao);

            //Quantidade Atividades
            TextView txtQuantidadeAtividades = view.findViewById(R.id.txtQuantVisitas);
            txtQuantidadeAtividades.setText("Quantidade Atividades: " +quantItens);

            //Data de Planejamento
            TextView txtDataPlanejamento = view.findViewById(R.id.txtDataPlanejamentoLocal);
            txtDataPlanejamento.setText("Data Programação: " + dataplanejamento);

            //Tempo Estimado Manutenção
            TextView txtTempoEstimado = view.findViewById(R.id.txtSLALocal);
            txtTempoEstimado.setText("Tempo Estimado: " + " 1:00 Hora");

            Calendar cal = Calendar.getInstance(); //
            cal.setTime(new Date()); //
            cal.add(Calendar.HOUR_OF_DAY, 1); // Adicionar Tempo Estimado
            cal.getTime(); //
            SimpleDateFormat datafim = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
            String horafim = datafim.format(cal.getTime());


            //Hora Inicio
            TextView txtHoraInicio = view.findViewById(R.id.txtHoraInicioVisita);
            txtHoraInicio.setText("Início: " + dataehora);

            //Hora Fim Estimada
            TextView txtHoraFim = view.findViewById(R.id.txtHoraFimVisita);
            txtHoraFim.setText("Fim: " + horafim);


            Button acceptButton = view.findViewById(R.id.acceptButton);
            Button btnUltimaVisita = view.findViewById(R.id.btnUltimaVisita);


            //Se clicar para iniciar Visita irá para Tela Atividades
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences pref = mContext.getSharedPreferences("info", MODE_PRIVATE);
                    String name = pref.getString("name", "");
                    String email= pref.getString("email", "");
                    String colaborador_id = pref.getString("id", "" );
                    String token = pref.getString("token", "");

                    Intent intent = new Intent(mContext, MainActivityAtividades.class);
                    Bundle dados = new Bundle();
                    dados.putString("os_id", id);
                    dados.putString("equipamento_id", equipamento_id);
                    dados.putString("local_id", local_id);
                    dados.putString("checklist_id", checklist_id);
                    dados.putString("tiposervico", tiposervico);
                    dados.putString("dataplanejamento", dataplanejamento);
                    dados.putString("id_centrolucro", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("idColaborador", colaborador_id);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    mContext.startActivity(intent);

                }
            });

            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setView(view)
                    .create();

            alertDialog.show();
        });
        }
        }
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