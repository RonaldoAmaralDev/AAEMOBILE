package br.com.araujoabreu.timg.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.visitas.Atividade_Antes;

import static android.content.Context.MODE_PRIVATE;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private String tiposervico, id_Atividade, tiposervico_id, tiposolicitacaoDescricao, tiposervicoDescricao, checklist_id, local_id, dataplanejamento, equipamento_id, os_id, id_centrolucro, atividade, idAtividade;

    BancoGeral myBDGeral;


    public AtividadeAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

        myBDGeral = new BancoGeral(mContext);

        SharedPreferences pref = mContext.getSharedPreferences("visita", MODE_PRIVATE);
        os_id = pref.getString("os_id", "");
        equipamento_id = pref.getString("equipamento_id", "" );
        local_id = pref.getString("local_id", "");
        dataplanejamento = pref.getString("dataplanejamento", "");
        tiposervico = pref.getString("tiposervico", "");
        id_centrolucro = pref.getString("id_centrolucro", "");


    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.descricaoatividade);
        }
    }

    @Override
    public AtividadeAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.atividade_row_line , parent, false);
        return new AtividadeAdapter.GroceryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AtividadeAdapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String idAtividades = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_ITEN));
        String descricaoAtividade = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_ITEN));
        checklist_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CHECKLIST_ITEN));

        holder.descricao.setText(descricaoAtividade);

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8

            Intent intent = new Intent(mContext, Atividade_Antes.class);
            Bundle dados = new Bundle();
            dados.putString("os_id", os_id);
            dados.putString("checklist_id", checklist_id);
            dados.putString("equipamento_id", equipamento_id);
            dados.putString("local_id", local_id);
            dados.putString("dataplanejamento", dataplanejamento);
            dados.putString("tiposervico", tiposervico);
            dados.putString("id_centrolucro", id_centrolucro);
            dados.putString("id_Atividade", idAtividades);
            dados.putString("atividade", descricaoAtividade);
            intent.putExtras(dados);
            mContext.startActivity(intent);

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