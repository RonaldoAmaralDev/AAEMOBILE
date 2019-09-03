package com.delaroystudios.uploadmedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.operacao.equipamento.MainActivityEquipamentos;

/**
 * Created by ravi on 16/11/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;




    public ContactsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView descricao;
        public TextView bairro;
        public TextView cidade;
        public TextView enderecolocal;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name);
            descricao = itemView.findViewById(R.id.phone);
            bairro = itemView.findViewById(R.id.bairrolocal);
            cidade = itemView.findViewById(R.id.cidadelocal);
            enderecolocal = itemView.findViewById(R.id.enderecolocal);
        }
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.local_row_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_LOCAL));
        String name = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CODIGO_LOCAL));
        String descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_LOCAL));
        String bairro = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_BAIRRO_LOCAL));
        String cidade = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CIDADE_LOCAL));
        String centrolucro_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CENTROCUSTO_LOCAL));
        String enderecolocal = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ENDERECO_LOCAL));

        holder.nameText.setText("Codigo: " + name);
        holder.descricao.setText("Descrição: " + descricao);
        holder.enderecolocal.setText("Endereço: " + enderecolocal);
        holder.bairro.setText("Bairro: " + bairro);
        holder.cidade.setText("Cidade: " + cidade);

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8
            Intent intent = new Intent(mContext, MainActivityEquipamentos.class);
            Bundle dados = new Bundle();
            dados.putString("local_id", id);
            dados.putString("centrolucro_id", centrolucro_id);
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